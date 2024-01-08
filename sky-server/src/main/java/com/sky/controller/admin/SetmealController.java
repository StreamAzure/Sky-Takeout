package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    private static final String cacheNames = "setmealCache";

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = cacheNames, key = "#setmealDTO.categoryId")
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐：{}", setmealDTO);
        setmealService.add(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result queryById(@PathVariable Long id){
        log.info("根据id查询套餐: {}", id);
        SetmealVO setmealVO = setmealService.queryById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = cacheNames, key = "#setmealDTO.categoryId")
    public Result edit(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐: {}", setmealDTO);
        setmealService.edit(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = cacheNames, allEntries = true)
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("套餐批量删除：{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐启售、停售")
    @CacheEvict(cacheNames = cacheNames, allEntries = true)
    public Result changeStatus(@PathVariable int status, Long id){
        log.info("套餐启售、停售: {}", status);
        setmealService.changeStatus(status, id);
        return Result.success();
    }

}
