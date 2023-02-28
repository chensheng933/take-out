package com.itheima.reggie.controller;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("dish")
public class DishController {

    @Autowired
    DishService dishService;

    /**
     * 保存菜品
     * @param dishDto
     * @param session
     * @return
     */
    @PostMapping
    public R save(@RequestBody DishDto dishDto, HttpSession session){
        //1.获取登陆者id
        Long userId = (Long) session.getAttribute("employee");

        //2.给dishDto设置创建人的信息
        dishDto.setCreateUser(userId);
        dishDto.setUpdateUser(userId);

        //3.调用service完成保存
        dishService.save(dishDto);

        //4.返回
        return R.success(null);
    }

    /**
     * 查询菜品分页
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R findByPage(@RequestParam("page") int pageNum,int pageSize,String name){
        //1.调用service完成查询操作
        Page<DishDto> page = dishService.findByPage(pageNum,pageSize,name);

        //2.将返回值封装返回
        return R.success(page);
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R findById(@PathVariable Long id){
        //1.调用service完成查询操作
        DishDto dishDto = dishService.findById(id);

        //2.将查询的dishDto封装R中返回
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @param session
     * @return
     */
    @PutMapping
    public R update(@RequestBody DishDto dishDto,HttpSession session){
        //1.获取登陆人id
        Long userId = (Long) session.getAttribute("employee");

        //2.修改dishDto中修改人的信息
        dishDto.setUpdateUser(userId);

        //3.调用service完成更新操作
        dishService.update(dishDto);

        //4.返回R.success
        return R.success(null);
    }

    @GetMapping("list")
    public R findOnSaleByCategoryId(Long categoryId){
        //1.调用service查询列表
        List<DishDto> list = dishService.findOnSaleByCategoryId(categoryId);

        //2.用R封装列表返回
        return R.success(list);
    }
}
