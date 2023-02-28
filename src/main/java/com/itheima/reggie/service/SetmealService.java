package com.itheima.reggie.service;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService {
    void save(SetmealDto setmealDto);

    Page<SetmealDto> findByPage(int pageNum, int pageSize, String name);

    void batchDelete(Long[] ids);

    List<Setmeal> findOnSaleByCategoryId(Long categoryId, Integer status);
}
