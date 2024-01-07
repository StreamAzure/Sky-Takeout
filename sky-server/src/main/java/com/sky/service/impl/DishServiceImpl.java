package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Override
    @Transactional // 事务注解
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        // 向菜品表插入1条数据
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // insert 操作完成后马上拿到主键值
        Long dishId = dish.getId();
        // 向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            // 每个口味对应同一个 dishId
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));
            dishFlavorMapper.insertBatch(flavors);
        }

    }
}
