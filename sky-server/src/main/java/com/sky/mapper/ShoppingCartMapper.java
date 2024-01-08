package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 根据要求（userid, dishId, setmealId）等查询购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据 ID 更新购物车商品数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据购物车项目 id 删除项目
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void delete(ShoppingCart shoppingCart);

    /**
     * 根据用户ID删除其购物车所有项目
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(ShoppingCart shoppingCart);
}
