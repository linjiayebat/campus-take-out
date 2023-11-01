package com.sky.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    // select setmeal_id from . where dish_id in
    List<Long> getSetMealId(List<Long> ids);
}
