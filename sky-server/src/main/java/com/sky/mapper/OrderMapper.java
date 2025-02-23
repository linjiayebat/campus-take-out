package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 处理支付超时订单
     * @param status
     * @param now
     */
    @Select("select * from orders where status = #{status} and order_time < #{now}")
    List<Orders> getTimeOutOrder(Integer status, LocalDateTime now);

    /**
     * 查询订单号
     * @param id
     * @return
     */
    @Select("select number from  orders where id = #{id}")
    String getNumberById(Long id);

    /**
     * 查询历史订单
     * @param status
     * @return
     */
    Page<Orders> selectHistoryOrders(Integer status, Long userId);

    /**
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 查询指定时间的营业额
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("select SUM(amount) from orders where order_time > #{beginTime} and order_time < #{endTime}")
    Double turnoverStatistics(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 查询指定时间的订单数/有效订单数
     * @param beginTime
     * @param endTime
     * @param status
     * @return
     */
    Integer selectOrderNumberWithStatus(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

    /**
     * 根据动态条件统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);
}
