package com.itheima.reggie.service;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;

public interface EmployeeService {
    R<Employee> login(Employee param);

    void save(Employee employee);

    Page<Employee> findByPage(int pageNum, int pageSize, String name);

    void update(Employee employee);

    Employee findById(int id);
}
