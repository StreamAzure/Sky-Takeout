package com.sky.controller.admin;

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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        // 分类下菜品列表有变动，清空缓存
        Long categoryId = dishDTO.getCategoryId();
        String key = "dish_" + categoryId;
        cleanCache(key);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询： {}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);
        // 删除所有菜品的缓存数据（省得再查具体哪些分类有改动，干脆都删了）
        cleanCache("dish_*");
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result edit(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}", dishDTO);
        dishService.editWithFlavor(dishDTO);
        // 删除所有菜品的缓存数据（省得再查具体哪些分类有改动，干脆都删了）
        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> queryById(@PathVariable Long id){
        log.info("根据id查询菜品: {}", id);
        DishVO dishVO = dishService.queryById(id);
        return Result.success(dishVO);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品启售、停售")
    public Result changeStatus(@PathVariable int status, Long id){
        log.info("菜品启售、停售：{}", status);
        dishService.changeStatus(status, id);
        // 删除所有菜品的缓存数据（省得再查具体哪些分类有改动，干脆都删了）
        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result queryByCategoryId(Long categoryId){
        log.info("根据分类id查询菜品: {}", categoryId);
        List<Dish> dishes = dishService.queryByCategoryId(categoryId);
        return Result.success(dishes);
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
        log.info("清空缓存：{}", pattern);
    }
}
