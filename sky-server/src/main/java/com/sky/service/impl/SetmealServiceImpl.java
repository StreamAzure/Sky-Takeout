package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.entity.Dish;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Set;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        // 套餐名是否重复
        Setmeal setmeal = setmealMapper.getByName(setmealDTO.getName());
        if(setmeal != null){
            throw new BaseException(MessageConstant.SETMEAL_NAME_ALREADY_EXISTS);
        }
        // 分类为null 或不存在？
        if(setmeal.getCategoryId() == null || categoryMapper.getNameById(setmeal.getCategoryId()) == null){
            throw new BaseException(MessageConstant.SETMEAL_CATEGORY_ERROR);
        }
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 套餐无菜品？
        if(setmealDishes == null || setmealDishes.isEmpty()){
            throw new BaseException(MessageConstant.SETMEAL_DISHES_EMPTY);
        }
        // 向setmeal插入一条数据
        setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 新增套餐默认停售状态
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.insert(setmeal);
        // 拿套餐id
        Long setmealId = setmeal.getId();
        // 向setmeal_dish批量插入菜品数据
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public SetmealVO queryById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        // 分类名称
        setmealVO.setCategoryName(categoryMapper.getNameById(setmealVO.getCategoryId()));
        // 关联菜品列表
        List<SetmealDish> dishes = setmealDishMapper.getDishesBySetmealId(id);
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }

    @Override
    public void edit(SetmealDTO setmealDTO) {
        // 套餐名是否重复
        Setmeal setmeal = setmealMapper.getByName(setmealDTO.getName());
        if(setmeal != null && setmeal.getId() != setmealDTO.getId()){
            throw new BaseException(MessageConstant.SETMEAL_NAME_ALREADY_EXISTS);
        }
        // 分类为null 或不存在？
        if(setmeal.getCategoryId() == null || categoryMapper.getNameById(setmeal.getCategoryId()) == null){
            throw new BaseException(MessageConstant.SETMEAL_CATEGORY_ERROR);
        }
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 套餐无菜品？
        if(setmealDishes == null || setmealDishes.isEmpty()){
            throw new BaseException(MessageConstant.SETMEAL_DISHES_EMPTY);
        }

        // 更新 setmeal 表
        setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        // 更新 setmeal_dish 表
        setmealDishMapper.deleteDishes(setmeal.getId());
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids){
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE) {
                // 启售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        for (Long id : ids) {
            // 删除套餐数据
            setmealMapper.deleteById(id);
            // 删除关联的 setmeal_dish 数据
            setmealDishMapper.deleteDishes(id);
        }
    }

    @Override
    public void changeStatus(int status, Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        // 若套餐启售，但包含未启售菜品，不能启售
        if (status == StatusConstant.ENABLE){
            List<SetmealDish> setmealDishes = setmealDishMapper.getDishesBySetmealId(id);
            for(SetmealDish setmealDish : setmealDishes){
                Dish dish = dishMapper.getById(setmealDish.getDishId());
                if(dish.getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
