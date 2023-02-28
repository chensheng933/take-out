package com.itheima.reggie.service;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService {

    void save(DishDto dishDto);

    Page<DishDto> findByPage(int pageNum, int pageSize, String name);

    DishDto findById(Long id);

    void update(DishDto dishDto);

    List<DishDto> findOnSaleByCategoryId(Long categoryId);
}
