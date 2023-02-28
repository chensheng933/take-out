package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("add")
    public R add(@RequestBody ShoppingCart paramCart, HttpSession session){
        //1.获取当前登陆者id
        Long userId = (Long) session.getAttribute("user");

        //2.给购物项设置用户id
        paramCart.setUserId(userId);

        //3.调用service完成添加操作,返回 购物项
        ShoppingCart shoppingCart = shoppingCartService.add(paramCart);

        //4.将 购物项 放入r中返回
        return R.success(shoppingCart);
    }

    @GetMapping("list")
    public R showCart(HttpSession session){
        //1.获取当前登陆者id
        Long userId = (Long) session.getAttribute("user");

        //2.调用service查询我的购物项列表
        List<ShoppingCart> list = shoppingCartService.showCart(userId);

        //3.将列表封装成R 返回
        return R.success(list);
    }

    @DeleteMapping("clean")
    public R cleanCart(HttpSession session){
        //1.获取当前登陆者id
        Long userId = (Long) session.getAttribute("user");

        //2.调用service删除我的购物项列表
        shoppingCartService.cleanCart(userId);

        //3.将列表封装成R 返回
        return R.success(null);
    }
}
