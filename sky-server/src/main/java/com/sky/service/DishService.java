package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {


    /**
     * 新增菜品 附加口味
     * @param dishDTO
     */
    void addDishWithFlavors(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult getDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteDish(List<Long> ids);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);
}
