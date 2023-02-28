package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("套餐dto")
public class SetmealDto extends Setmeal {

    @ApiModelProperty("套餐中菜品集合")
    private List<SetmealDish> setmealDishes;//套餐关联的菜品集合

    @ApiModelProperty("所属分类的名称")
    private String categoryName;//分类名称
}