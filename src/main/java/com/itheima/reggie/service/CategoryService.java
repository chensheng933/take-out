package com.itheima.reggie.service;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.entity.Category;

import java.util.List;

public interface CategoryService {
    Page<Category> findByPage(int pageNum, int pageSize);

    void save(Category category);

    void update(Category category);

    void delete(Long id);

    List<Category> findByType(Integer type);

    List<Category> list();
}
