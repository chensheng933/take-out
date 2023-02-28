package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    @Select("select * from employee where username = #{username}")
    Employee findByName(String username);

    @Insert("insert into employee values (null,#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Employee employee);

    List<Employee> findByPage(String name);

    void update(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee findById(int id);
}
