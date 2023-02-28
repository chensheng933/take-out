package com.itheima.reggie.controller;

import com.itheima.reggie.common.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    /**
     * 保存套餐
     * @param setmealDto
     * @param session
     * @return
     */
    @PostMapping
    //清空SETMEAL下的所有缓存
    @CacheEvict(value = "SETMEAL",allEntries = true)
    public R save(@RequestBody SetmealDto setmealDto, HttpSession session){
        //1.获取登陆者id
        Long userId = (Long) session.getAttribute("employee");

        //2.给 setmealDto 设置俩人信息
        setmealDto.setCreateUser(userId);
        setmealDto.setUpdateUser(userId);

        //3.调用service完成新增操作
        setmealService.save(setmealDto);

        //4.返回
        return R.success(null);
    }

    @GetMapping("page")
    @Cacheable(value = "SETMEAL",key = "'PAGE:'+#pageNum+'_'+#pageSize+'_'+#name"
            ,unless = "#result.data.records.size()==0")
    @ApiOperation("分页查询套餐列表")
    //参数进行描述
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "查询的页码",required = true,defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示的条数",required = true,defaultValue = "10"),
            @ApiImplicitParam(name = "name",value = "套餐名称",required = false)
    })
    public R findByPage(@RequestParam("page") int pageNum,int pageSize,String name){
        //1.调用service完成分页查询,返回Page对象
        Page<SetmealDto> page = setmealService.findByPage(pageNum,pageSize,name);

        //2.使用r封装page且返回
        return R.success(page);
    }

    @DeleteMapping
    //参数可以使用集合或者数组,但是若使用集合需要在参数前@RequestParam
    //清空SETMEAL下的所有缓存
    @CacheEvict(value = "SETMEAL",allEntries = true)
    public R batchDelete(Long[] ids){
        setmealService.batchDelete(ids);
        return R.success(null);
    }

    @GetMapping("list")
    @Cacheable(value = "SETMEAL",key = "'ONSALE:'+#categoryId",unless = "#result.data.size()==0")
    public R findOnSaleByCategoryId(Long categoryId,Integer status){
        List<Setmeal> list = setmealService.findOnSaleByCategoryId(categoryId,status);
        return R.success(list);
    }
}
