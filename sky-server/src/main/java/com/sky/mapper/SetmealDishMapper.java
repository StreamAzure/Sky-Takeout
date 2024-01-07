package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品 ID 查询套餐 ID
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入菜品信息
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID查询包含的菜品列表
     * @param setmealId
     * @return
     */
    List<SetmealDish> getDishesBySetmealId(Long setmealId);

    /**
     * 根据套餐ID删除其全部菜品
     * @param setmealId
     */
    @Delete("DELETE FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
    void deleteDishes(Long setmealId);
}
