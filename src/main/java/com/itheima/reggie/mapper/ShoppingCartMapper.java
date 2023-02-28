package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShoppingCartMapper {
    ShoppingCart findByDishIdOrSetmealIdWithUserId(ShoppingCart paramCart);

    @Insert("insert into shopping_cart values (null,#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{createTime})")
    void save(ShoppingCart shoppingCart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{userId} order by create_time asc")
    List<ShoppingCart> findAllByUserId(Long userId);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}
