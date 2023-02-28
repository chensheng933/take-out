package com.itheima.reggie.service.impl;

import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart add(ShoppingCart paramCart) {
        //1.通过用户id和菜品或套餐id进行查询购物项
        ShoppingCart shoppingCart = shoppingCartMapper.findByDishIdOrSetmealIdWithUserId(paramCart);

        //2.判断当前用户有无添加过此购物项,
        if (shoppingCart == null) {
            //2.1 若没有加过,将此购物项保存到数据库中(设置数量和创建时间)
            shoppingCart = paramCart;
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.save(shoppingCart);
        }else{
            //2.2 若加过,修改数量(+1),更新数据库
            shoppingCart.setNumber(shoppingCart.getNumber()+1);

            shoppingCartMapper.update(shoppingCart);
        }
        //3.返回购物项
        return shoppingCart;
    }

    @Override
    public List<ShoppingCart> showCart(Long userId) {
        //调用mapper查询
        return shoppingCartMapper.findAllByUserId(userId);
    }

    @Override
    public void cleanCart(Long userId) {
        shoppingCartMapper.deleteByUserId(userId);
    }
}
