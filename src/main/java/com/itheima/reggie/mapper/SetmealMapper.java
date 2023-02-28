package com.itheima.reggie.mapper;

import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

public interface SetmealMapper {

    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> findByCategoryId(Long categoryId);

    @Insert("insert into setmeal values (null,#{categoryId},#{name},#{price},#{status},#{code},#{description},#{image},#{createTime},#{updateTime},#{createUser},#{updateUser},0)")
    @SelectKey(statement = "select last_insert_id() from dual",before = false,keyColumn = "id",keyProperty = "id",resultType = Long.class)
    void save(SetmealDto setmealDto);

    List<Setmeal> findAll(String name);

    List<Setmeal> findOnSaleByIds(Long[] ids);

    void batchDelete(Long[] ids);
}
