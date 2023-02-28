package com.itheima.reggie.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.reggie.common.ConstantUtil;
import com.itheima.reggie.common.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

//静态导入
import static com.itheima.reggie.common.ConstantUtil.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 用户登录
     * @param param
     * @return
     */
    @Override
    public R<Employee> login(Employee param) {
        //1.获取用户提交过来的用户名和密码
        String username = param.getUsername();
        String password = param.getPassword();

        //2.判断用户名是否为空,若为空返回错误R  "    "
        //if (username == null || username.trim().length()==0) {}
        //strutil是hutool工具类提供的
        if (StrUtil.isBlank(username)) {
            return R.error("用户名不能为空");
        }

        //3.调用dao的findByName方法,返回employee,判断employee是否为空,若为空返回错误R
        Employee employee = employeeMapper.findByName(username);
        if (employee == null) {
            return R.error("用户名不存在");
        }

        //4.判断密码是否正确,若不正确返回错误R
        //if (password==null || !password.equals(employee.getPassword())) {}
        //SecureUtil 使用hutool工具类中的方法
        if (!StrUtil.equals(SecureUtil.md5(password),employee.getPassword())) {
            return R.error("密码输入错误");
        }

        //5.判断用户是否被禁用,若被禁用返回错误R
        if (employee.getStatus() != 1) {
            return R.error("用户已被禁用,请联系店长~");
        }

        //6.为了保证数据的安全,将密码设置为null,返回正确的R
        employee.setPassword(null);
        return R.success(employee);
    }

    /**
     * 新增员工
     * @param employee
     */
    @Override
    public void save(Employee employee) {
        //1.封装employee
        //employee.setPassword(SecureUtil.md5("123456"));
        //employee.setPassword(SecureUtil.md5(ConstantUtil.INIT_PASSWORD));
        employee.setPassword(SecureUtil.md5(INIT_PASSWORD));
        employee.setStatus(STATUS_ON);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(employee.getCreateTime());

        //2.调用mapper完成保存操作
        employeeMapper.save(employee);
    }

    /**
     * 员工分页列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<Employee> findByPage(int pageNum, int pageSize, String name) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.调用mapper查询(默认就会分页),返回list
        List<Employee> list = employeeMapper.findByPage(name);

        //3.将list封装到paegInfo中,可以获取到total
        PageInfo<Employee> pageInfo = new PageInfo<>(list);

        //4.封装Page返回
        return new Page<>(list,pageInfo.getTotal());
    }

    /**
     * 更新员工信息
     * @param employee
     */
    @Override
    public void update(Employee employee) {
        //设置页面上没有传过来的数据
        employee.setUpdateTime(LocalDateTime.now());

        //调用dao完成更新操作
        employeeMapper.update(employee);
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    public Employee findById(int id) {
        return employeeMapper.findById(id);
    }
}
