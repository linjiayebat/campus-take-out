package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 处理异常 地址为空 或者 购物车为空 无法下单
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getById(addressBookId);
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list == null || list.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        // 向订单表插入数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(String.valueOf(addressBook));
        orders.setAddressBookId(addressBookId);
        orders.setPhone(addressBook.getPhone());
        orderMapper.insert(orders);

        // 向订单详细表插入n条数据

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        for (ShoppingCart shoppingCart1 : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart1, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.insertBatch(orderDetail);
        }

        // 清空购物车
        shoppingCartMapper.clean(shoppingCart);

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setOrderTime(LocalDateTime.now());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setId(orders.getId());

        return orderSubmitVO;


//        //异常情况的处理（收货地址为空、购物车为空）
//        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
//        if (addressBook == null) {
//            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
//        }
//
//        Long userId = BaseContext.getCurrentId();
//        ShoppingCart shoppingCart = new ShoppingCart();
//        shoppingCart.setUserId(userId);
//
//        //查询当前用户的购物车数据
//        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
//        if (shoppingCartList == null || shoppingCartList.size() == 0) {
//            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
//        }
//
//        //构造订单数据
//        Orders order = new Orders();
//        BeanUtils.copyProperties(ordersSubmitDTO,order);
//        order.setPhone(addressBook.getPhone());
//        order.setAddress(addressBook.getDetail());
//        order.setConsignee(addressBook.getConsignee());
//        order.setNumber(String.valueOf(System.currentTimeMillis()));
//        order.setUserId(userId);
//        order.setStatus(Orders.PENDING_PAYMENT);
//        order.setPayStatus(Orders.UN_PAID);
//        order.setOrderTime(LocalDateTime.now());
//
//        //向订单表插入1条数据
//        orderMapper.insert(order);
//
//        //订单明细数据
//        List<OrderDetail> orderDetailList = new ArrayList<>();
//        for (ShoppingCart cart : shoppingCartList) {
//            OrderDetail orderDetail = new OrderDetail();
//            BeanUtils.copyProperties(cart, orderDetail);
//            orderDetail.setOrderId(order.getId());
//            orderDetailList.add(orderDetail);
//        }
//
//        //向明细表插入n条数据
//        orderDetailMapper.insertBatch(orderDetailList);
//
//        //清理购物车中的数据
//        shoppingCartMapper.deleteByUserId(userId);
//
//        //封装返回结果
//        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
//                .id(order.getId())
//                .orderNumber(order.getNumber())
//                .orderAmount(order.getAmount())
//                .orderTime(order.getOrderTime())
//                .build();
//
//        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

}
