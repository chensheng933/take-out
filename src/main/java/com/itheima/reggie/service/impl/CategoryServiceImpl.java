package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.reggie.common.Page;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.exception.BusinessException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public Page<Category> findByPage(int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        //2.调用mapper进行查询
        List<Category> list = categoryMapper.findAll();

        //3.将查询list封装给PageInfo
        PageInfo<Category> pageInfo = new PageInfo<>(list);

        //4.封装Page且返回
        return new Page<>(list,pageInfo.getTotal());
    }

    @Override
    public void save(Category category) {
        //1.补全对象数据
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(category.getCreateTime());

        //2.调用mapper完成保存
        categoryMapper.save(category);
    }

    @Override
    public void update(Category category) {
        //1.设置更新时间
        category.setUpdateTime(LocalDateTime.now());

        //2.调用mapper完成更新
        categoryMapper.update(category);
    }

    @Override
    public void delete(Long id) {
        //1.判断分类下有无菜品,若有抛异常(不能删除)
        List<Dish> dishList = dishMapper.findByCategoryId(id);

        //if (dishList!=null && dishList.size()>0) {}
        if (CollUtil.isNotEmpty(dishList)) {
            throw new BusinessException("该分类下有菜品,不能删除~");
        }

        //2.判断分类下有无套餐,若有抛异常(不能删除)
        List<Setmeal> setmealList = setmealMapper.findByCategoryId(id);
        if (CollUtil.isNotEmpty(setmealList)) {
            throw new BusinessException("该分类下有套餐,不能删除~");
        }

        //调用mapper完成删除
        categoryMapper.delete(id);
    }

    @Override
    public List<Category> findByType(Integer type) {
        return categoryMapper.findByType(type);
    }

    @Override
    public List<Category> list() {
        return categoryMapper.getList();
    }
}
