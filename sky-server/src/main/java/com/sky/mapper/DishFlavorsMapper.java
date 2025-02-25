package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {

    /**
     * 新增菜品对应口味
     * @param dishFlavor
     */

    void addFlavors(List<DishFlavor> dishFlavor);

    void deleteDishFlavors(List<Long> ids);

    /**
     * 根据菜品id查询对应口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}
