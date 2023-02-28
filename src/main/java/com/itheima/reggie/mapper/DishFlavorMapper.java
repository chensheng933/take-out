package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DishFlavorMapper {
    void batchSave(List<DishFlavor> flavors);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> findByDishId(Long dishId);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void batchDeleteByDishId(Long dishId);
}
