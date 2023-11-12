package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 新增购物车
     * @param shoppingCart
     */
    void add(ShoppingCart shoppingCart);

    /**
     * 查询购物车
     * @param shoppingCart
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新购物车 数量加1
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart shoppingCart);

    /**
     * 删除购物车记录
     * @param shoppingCart1
     */
    void delete(ShoppingCart shoppingCart1);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(ShoppingCart shoppingCart);
}
