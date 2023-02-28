package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

public interface UserMapper {
    @Select("select * from user where phone = #{phone}")
    User findByPhone(String phone);

    @Insert("insert into user (phone,status) values (#{phone},#{status})")
    @SelectKey(statement = "select last_insert_id()",before = false,keyProperty = "id",keyColumn = "id",resultType = Long.class)
    void save(User user);

    @Select("select * from user where id = #{id}")
    User findById(Long userId);

    @Select("select  * from user")
    List<User> findAll();

}
