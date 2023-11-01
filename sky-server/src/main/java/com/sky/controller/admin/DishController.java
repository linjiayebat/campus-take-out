package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api("菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDishWithFlavors(dishDTO);
        return Result.success();
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
        return Result.success();
    }

    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }
}
