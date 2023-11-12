package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);

    /**
     * 菜品分页拆线呢
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> getDish(DishPageQueryDTO dishPageQueryDTO);

    void deleteDish(List<Long> ids);

    @Select("select status from dish where  id = #{id}")
    int getDishStatus(Long id);

    void updateDish(DishDTO dishDTO);

    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    List<Long> getCategoryIds(List<Long> ids);

    @Update("update dish set status = #{status} where id = #{id}")
    void bpDish(Integer status, Long id);

    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);
}