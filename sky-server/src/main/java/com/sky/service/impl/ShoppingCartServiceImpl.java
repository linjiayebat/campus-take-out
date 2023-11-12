package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {


    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 新增购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        // 根据菜品id或者套餐id以及用户id查询是否购物车存在菜品或者套餐
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && list.size() > 0) {
            ShoppingCart shoppingCart1 = list.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.update(shoppingCart1);
        } else {
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);

            BigDecimal price;
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId());
                price = dish.getPrice();
            } else {
                Setmeal setmeal = setmealMapper.selctById(shoppingCartDTO.getSetmealId());
                price = setmeal.getPrice();
            }

            shoppingCart.setAmount(price);
            shoppingCartMapper.add(shoppingCart);
        }
    }

    /**
     * 查看购物车
     */
    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 删除购物车 数量减一 或者删除记录
     * @param shoppingCartDTO
     */
    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if (list != null && list.size() > 0) {
            ShoppingCart shoppingCart1 = list.get(0);
            if (shoppingCart1.getNumber() == 1) {
                shoppingCartMapper.delete(shoppingCart1);
            } else {
                shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
                shoppingCartMapper.update(shoppingCart1);
            }
        }

    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCartMapper.clean(shoppingCart);
    }
}
