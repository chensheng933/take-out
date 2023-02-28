package com.itheima.reggie.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PageHelperTest {

    @Autowired
    EmployeeMapper employeeMapper;

    @Test
    public void testFindByPage() {
        int pageNum = 2;
        int pageSize = 5;
        String name = "员工";

        //1.设置分页参数,调用PageHelper静态方法
        PageHelper.startPage(pageNum,pageSize);

        //2.调用mapper的select方法(紧跟着PageHelper静态方法的第一条select语句会被分页),返回list
        List<Employee> list = employeeMapper.findByPage(name);



        //3.将list使用PageInfo进行封装从而获取分页中各种信息
        PageInfo<Employee> pageInfo = new PageInfo<>(list);

        System.out.println("总记录数:"+pageInfo.getTotal());
        System.out.println("开始页码:"+pageInfo.getPageNum());
        System.out.println("每页条数:"+pageInfo.getPageSize());
        System.out.println("总页数:"+pageInfo.getPages());
        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

}
