package com.itheima.reggie.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    private Long id;  //用户id

    private String username;  //登录的时候使用用户名

    private String name;  //昵称

    private String password;  //密码

    private String phone;  //手机号

    private String sex;  //性别

    private String idNumber; //驼峰命名法 ---> 映射的字段名为 id_number

    private Integer status; //用户的状态  0禁用 1启用

    private LocalDateTime createTime;  //创建用户的时间

    private LocalDateTime updateTime; //修改用户的时间

    private Long createUser;  //创建用户的人

    private Long updateUser; //修改用户的人
}