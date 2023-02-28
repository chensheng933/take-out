package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SetmealDishMapper {
    void batchSave(List<SetmealDish> setmealDishes);

    void batchDeleteBySetmealIds(Long[] ids);

    @Select("select * from setmeal where category_id = #{cid} and status = #{status}")
    List<Setmeal> findOnSaleByCategoryId(@Param("cid") Long categoryId, @Param("status") Integer status);
}
