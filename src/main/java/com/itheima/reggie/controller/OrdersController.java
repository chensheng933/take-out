package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @PostMapping("submit")
    public R save(@RequestBody Orders order, HttpSession session){
        //1.获取登录人id
        Long userId = (Long) session.getAttribute("user");

        //2.给order设置所属人id
        order.setUserId(userId);

        //3.调用service完成生成订单操作
        ordersService.save(order);

        //4.返回
        return R.success(null);
    }
}
