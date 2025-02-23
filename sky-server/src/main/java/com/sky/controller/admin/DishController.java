package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api("菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDishWithFlavors(dishDTO);

        // 删除菜品缓存
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        List<DishVO> list = dishService.listWithFlavor(dish);
        return Result.success(list);
    }


    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> getDish(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询 {}", dishPageQueryDTO);
        PageResult pageResult = dishService.getDish(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping()
    public Result deleteDish(@RequestParam List<Long> ids) {
        log.info("删除菜品: {}", ids);
        dishService.deleteDish(ids);

        // 删除菜品缓存
        List<Long> categoryIds = dishService.getCategoryIds(ids);
        for (Long id : categoryIds) {
            String key = "dish_" + id;
            redisTemplate.delete(key);
        }

        return Result.success();
    }

    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        // 删除菜品缓存
        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result bpDish(@PathVariable Integer status, Long id) {
        dishService.bpDish(status, id);
        List<Long> templateId = new ArrayList<>();
        templateId.add(id);
        List<Long> categoryIds = dishService.getCategoryIds(templateId);
        for (Long t : categoryIds) {
            String key = "dish_" + t;
            redisTemplate.delete(key);
        }
        return Result.success();
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

}
