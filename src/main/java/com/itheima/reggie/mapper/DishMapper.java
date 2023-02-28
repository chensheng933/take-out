package com.itheima.reggie.mapper;

import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

public interface DishMapper {

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> findByCategoryId(Long categoryId);

    @Insert("insert into dish values(null,#{name},#{categoryId},#{price},#{code},#{image},#{description},#{status},#{sort},#{createTime},#{updateTime},#{createUser},#{updateUser},0)")
    //返回新增记录的主键

    // 只适用于主键的数据库(oracle不适用)
    //@Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")

    //使用于所有数据库
    //在插入语句执行之"后"(mysql是后,oracle是前),发送"select last_insert_id() from dual"查询新增记录的主键,将id列的值返回,设置给对象的id属性
    @SelectKey(statement = "select last_insert_id() from dual",before = false,keyProperty = "id",keyColumn = "id",resultType = Long.class)
    void save(DishDto dishDto);

    List<Dish> findAll(String name);

    @Select("select * from dish where id = #{id}")
    Dish findById(Long id);

    void update(DishDto dishDto);

    @Select("select * from dish where category_id = #{categoryId} and status = 1")
    List<Dish> findOnSaleByCategoryId(Long categoryId);
}
