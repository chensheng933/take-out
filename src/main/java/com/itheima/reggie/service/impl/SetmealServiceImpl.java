package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.reggie.common.Page;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.exception.BusinessException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.SetmealDishMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SetmealDto setmealDto) {
        //1.往setmeal中新增一条记录
        //1.1 给dto设置俩时间
        setmealDto.setCreateTime(LocalDateTime.now());
        setmealDto.setUpdateTime(setmealDto.getCreateTime());

        //1.2 调用setmealMapper完成保存,别忘记返回主键
        setmealMapper.save(setmealDto);

        //2.往setmeal_dish中新增多条记录
        //2.1 从dto中获取菜品集合
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        //2.2 遍历集合,获取到每个菜品,
        if (CollUtil.isNotEmpty(setmealDishes)) {
            for (SetmealDish setmealDish : setmealDishes) {
                //2.3 封装菜品信息 setmeal_id sort 俩时间 俩人
                setmealDish.setSetmealId(setmealDto.getId());
                setmealDish.setSort(1);

                setmealDish.setCreateTime(setmealDto.getCreateTime());
                setmealDish.setUpdateTime(setmealDto.getUpdateTime());

                setmealDish.setCreateUser(setmealDto.getCreateUser());
                setmealDish.setUpdateUser(setmealDto.getUpdateUser());
            }

            //2.4 调用setmealDishMapper完成批量保存
            setmealDishMapper.batchSave(setmealDishes);
        }
    }

    @Override
    public Page<SetmealDto> findByPage(int pageNum, int pageSize, String name) {
        //1.先查询套餐的分类list,转成map key:分类id  value:分类名称
        List<Category> categoryList = categoryMapper.findByType(2);
        Map<Long,String> categoryMap = new HashMap<>();
        for (Category category : categoryList) {
            categoryMap.put(category.getId(),category.getName());
        }

        //2.设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        //3.调用mapper查询list
        List<Setmeal> setmealList = setmealMapper.findAll(name);

        //4.使用pageInfo封装list,获取total
        PageInfo<Setmeal> pageInfo = new PageInfo<>(setmealList);

        //5.遍历list,获取每个套餐,将套餐封装成dto,设置分类名称
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(setmealList)) {
            for (Setmeal setmeal : setmealList) {
                SetmealDto setmealDto = new SetmealDto();

                //拷贝属性
                BeanUtils.copyProperties(setmeal,setmealDto);

                //设置分类名称
                setmealDto.setCategoryName(categoryMap.get(setmealDto.getCategoryId()));

                //将封装好的dto加入dtoList
                setmealDtoList.add(setmealDto);
            }
        }

        //6.封装且返回page
        return new Page<>(setmealDtoList,pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        //1.判断数组是否为空,若为空则提示
        if (ArrayUtil.isEmpty(ids)) {
            throw new BusinessException("请选择要删除的套餐~~~");
        }

        //2.判断这些套餐有无起售的,若有的话则提示
        List<Setmeal> list = setmealMapper.findOnSaleByIds(ids);
        if (CollUtil.isNotEmpty(list)) {
            throw new BusinessException("请先停售要删除的套餐~~~");
        }

        //3.从setmeal中删除这些套餐
        setmealMapper.batchDelete(ids);

        //4.从setmeal_dish删除相关的记录
        setmealDishMapper.batchDeleteBySetmealIds(ids);
    }

    @Override
    public List<Setmeal> findOnSaleByCategoryId(Long categoryId, Integer status) {
        return setmealDishMapper.findOnSaleByCategoryId(categoryId,status);
    }
}
