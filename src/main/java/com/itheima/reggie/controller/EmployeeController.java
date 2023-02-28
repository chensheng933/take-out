package com.itheima.reggie.controller;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping({"login","login1"})
    public R login(@RequestBody Employee param, HttpSession session){
        //1.调用service 完成登陆的查询操作
        R<Employee> r = employeeService.login(param);

        //2.若登陆成功了,记录用户的登陆状态(只把用户的id存入session中),返回成功R
        if (r.getCode()==1) {
            session.setAttribute("employee",r.getData().getId());
        }

        //3.若登陆失败了,需要返回错误R
        return r;
    }


    @PostMapping("logout")
    public R logout(HttpSession session){
        //删除当前会话
        session.invalidate();

        return R.success(null);
    }

    /**
     * 新增员工
     * @param employee 页面上传递过来的数据
     * @param session
     * @return
     */
    @PostMapping
    public R save(@RequestBody Employee employee,HttpSession session){
        //1.获取session中登陆者id
        Long userId = (Long) session.getAttribute("employee");

        //2.给employee设置创建人id和修改人id
        employee.setCreateUser(userId);
        employee.setUpdateUser(userId);

        //3.调用service完成保存操作
        employeeService.save(employee);

        return R.success(null);
    }

    /**
     * 分页查询
     * @param pageNum 查询的页码
     * @param pageSize 每页显示的条数
     * @param name 查询的员工名称
     * @return
     */
    @GetMapping("page")
    public R findByPage(@RequestParam("page") int pageNum,int pageSize,String name){
        Page<Employee> page = employeeService.findByPage(pageNum,pageSize,name);
        return R.success(page);
    }

    /**
     * 修改用户
     * @param employee 页面传过来的员工数据
     * @param session session会话域
     * @return
     */
    @PutMapping
    public R update(@RequestBody Employee employee,HttpSession session){
        //1.获取当前登陆人id
        Long userId = (Long) session.getAttribute("employee");

        //2.给employee设置更新人id
        employee.setUpdateUser(userId);

        //3.调用service完成更新操作
        employeeService.update(employee);
        return R.success(null);
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R findById(@PathVariable int id){
       Employee employee = employeeService.findById(id);
       return R.success(employee);
    }
}
