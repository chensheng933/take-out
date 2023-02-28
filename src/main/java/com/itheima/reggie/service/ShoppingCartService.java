package com.itheima.reggie.service;

import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCart add(ShoppingCart paramCart);

    List<ShoppingCart> showCart(Long userId);

    void cleanCart(Long userId);
}
