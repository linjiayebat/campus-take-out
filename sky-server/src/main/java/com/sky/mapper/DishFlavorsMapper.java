package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {

    /**
     * 新增菜品对应口味
     * @param dishFlavor
     */

    void addFlavors(List<DishFlavor> dishFlavor);

    void deleteDishFlavors(List<Long> ids);
}
