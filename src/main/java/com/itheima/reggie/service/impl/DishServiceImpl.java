package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.reggie.common.ConstantUtil;
import com.itheima.reggie.common.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.itheima.reggie.common.ConstantUtil.DISH_ONSALE;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)//遇到任何问题都回滚
    public void save(DishDto dishDto) {
        //1.往dish表中新增一条记录
        //1.1 给dto设置sort,俩时间字段
        dishDto.setSort(1);//随便写一个
        dishDto.setCreateTime(LocalDateTime.now());
        dishDto.setUpdateTime(dishDto.getCreateTime());

        //1.2 调用dishMapper完成保存,别忘记新增记录主键返回
        dishMapper.save(dishDto);

        //2.往dish_flavor表中新增多条记录
        //2.1 获取当前菜品的口味集合
        List<DishFlavor> flavors = dishDto.getFlavors();

        //2.2 遍历集合获取每个口味对象
        if (CollUtil.isNotEmpty(flavors)) {
            flavors.forEach(f->{
                //2.3 设置菜品id,时间和创建人
                f.setDishId(dishDto.getId());

                f.setCreateTime(dishDto.getCreateTime());
                f.setUpdateTime(dishDto.getUpdateTime());

                f.setCreateUser(dishDto.getCreateUser());
                f.setUpdateUser(dishDto.getUpdateUser());
            });

            //2.4 调用dishFlavorMapper批量保存
            dishFlavorMapper.batchSave(flavors);
        }

        //3.清除redis中缓存数据
        Set keySet = redisTemplate.keys(DISH_ONSALE + "*");
        redisTemplate.delete(keySet);
    }

    @Override
    public Page<DishDto> findByPage(int pageNum, int pageSize, String name) {
        //0.1 先查询所有的菜品分类 list
        List<Category> categoryList = categoryMapper.findByType(1);

        //0.2 将list转成成map  map的key:分类id,map的value:分类名称
        Map<Long,String> categoryMap = new HashMap<>();
        categoryList.forEach(c->{
            categoryMap.put(c.getId(),c.getName());
        });

        //1.设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        //2.调用mapper完成查询操作,返回list
        List<Dish> dishList = dishMapper.findAll(name);

        //3.用PageInfo封装list,获取total
        PageInfo<Dish> pageInfo = new PageInfo<>(dishList);

        //传统写法
        //4.遍历dishList,获取每个dish,将dish封装成dishDto,别忘记设置分类名称
        //4.1 创建dtoList
        List<DishDto> dtoList = new ArrayList<>();

        //4.2 编辑dishList,获取每个dish
        for (Dish dish : dishList) {
            DishDto dishDto = new DishDto();

            //4.3 属性对拷(只要属性名一致就可以复制进去,我们使用的spring的,带数据的对象在前)
            BeanUtils.copyProperties(dish,dishDto);

            //4.4 根据categoryId去查询categoryname即可
            dishDto.setCategoryName(categoryMap.get(dishDto.getCategoryId()));

            //4.5 将dto放入dtoList中
            dtoList.add(dishDto);
        }

        //lambad写法
        //4.遍历dishList,获取每个dish,将dish封装成dishDto,别忘记设置分类名称
        /*
        List<DishDto> dtoList = dishList.stream().map(dish -> {
            DishDto dishDto = new DishDto();

            //属性对拷(只要属性名一致就可以复制进去,我们使用的spring的,带数据的对象在前)
            BeanUtils.copyProperties(dish,dishDto);

            //根据categoryId去查询categoryname即可
            dishDto.setCategoryName(categoryMap.get(dishDto.getCategoryId()));

            //返回封装dto
            return dishDto;
        }).collect(Collectors.toList());
        */

        //5.封装成Page且返回
        return new Page<>(dtoList,pageInfo.getTotal());
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    public DishDto findById(Long id) {
        //1.根据id查询菜品
        Dish dish = dishMapper.findById(id);

        //2.根据id查询菜品口味
        List<DishFlavor> flavors = dishFlavorMapper.findByDishId(id);

        //3.封装Dto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DishDto dishDto) {
        //1.修改菜品
        //1.1 设置修改时间
        dishDto.setUpdateTime(LocalDateTime.now());

        //1.2 调用dishMapper完成更新
        dishMapper.update(dishDto);

        //2.删除菜品原来的口味
        dishFlavorMapper.batchDeleteByDishId(dishDto.getId());

        //3.保存菜品的现在口味
        //3.1 获取菜品中的口味集合,遍历它
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (CollUtil.isNotEmpty(flavors)) {
            for (DishFlavor flavor : flavors) {
                //3.2 给每个口味封装俩时间,俩用户,菜品id
                flavor.setUpdateUser(dishDto.getUpdateUser());
                flavor.setUpdateTime(dishDto.getUpdateTime());

                flavor.setCreateUser(dishDto.getCreateUser());
                flavor.setCreateTime(dishDto.getCreateTime());

                flavor.setDishId(dishDto.getId());
            }

            //3.3 批量保存
            dishFlavorMapper.batchSave(flavors);
        }

        //4.清除redis中缓存数据
        Set keySet = redisTemplate.keys(DISH_ONSALE + "*");
        redisTemplate.delete(keySet);
    }

    @Override
    public List<DishDto> findOnSaleByCategoryId(Long categoryId) {
        //0.先从redis中查询,若redis中有直接返回
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<DishDto> dtoList = (List<DishDto>) valueOperations.get(DISH_ONSALE+categoryId);

        if (CollUtil.isNotEmpty(dtoList)) {
            log.info("从redis中获取分类的菜品列表"+categoryId);
            return dtoList;
        }

        //0.1 若redis中没有数据在查询mysql中,查询完成之后别忘记把数据存入redis中再返回
        List<Dish> dishList = dishMapper.findOnSaleByCategoryId(categoryId);

        //遍历dishList,获取每个菜品dish,查询每个菜品的口味列表,将菜品和口味列表封装DishDto对象,将封装好的对象放入dtoList中
        //1.创建DtoLIst
        dtoList = new ArrayList<>();

        //2.遍历dishList,获取每个菜品dish
        if (CollUtil.isNotEmpty(dishList)) {
            for (Dish dish : dishList) {
                //3.查询每个菜品的口味列表
                List<DishFlavor> flavors = dishFlavorMapper.findByDishId(dish.getId());

                //4.将菜品和口味列表封装DishDto对象
                DishDto dto = new DishDto();
                BeanUtils.copyProperties(dish,dto);
                dto.setFlavors(flavors);

                //5.将封装好的对象放入dtoList中
                dtoList.add(dto);
            }
        }

        //3.把查询的列表存入redis中
        valueOperations.set(DISH_ONSALE+categoryId,dtoList);
        log.info("将菜品列表存入了redis"+categoryId);

        return dtoList;
    }
}
