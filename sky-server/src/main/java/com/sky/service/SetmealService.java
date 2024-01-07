package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void add(SetmealDTO setmealDTO);

    SetmealVO queryById(Long id);

    void edit(SetmealDTO setmealDTO);

    void deleteBatch(List<Long> ids);

    void changeStatus(int status, Long id);
}
