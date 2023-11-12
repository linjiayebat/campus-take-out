package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Category;
import com.sky.entity.ShoppingCart;
import com.sky.result.PageResult;

import java.util.List;

public interface ShoppingCartService {



    /**
     * 新增购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     */
    List<ShoppingCart> list();

    /**
     * 删除购物车 数量减一 或者删除记录
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);


    /**
     * 清空购物车
     */
    void clean();

}
