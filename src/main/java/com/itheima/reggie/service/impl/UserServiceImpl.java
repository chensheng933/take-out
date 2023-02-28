package com.itheima.reggie.service.impl;

import cn.hutool.core.util.StrUtil;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.exception.BusinessException;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public R<User> login(String phone, String requestCode, String sessionCode) {
        //1.用户有无输验证码,若无则提示
        if (StrUtil.isBlank(requestCode)) {
            throw new BusinessException("请输入验证码~~");
        }

        //2.用户无获取验证码,若无则提示
        if(StrUtil.isBlank(sessionCode)){
            throw new BusinessException("请先获取验证码~~");
        }

        //3.判断验证码是否输入正确,若不正确则提示
        if (!StrUtil.equals(requestCode,sessionCode)) {
            throw new BusinessException("请输入正确验证码~~");
        }

        //4.调用mapper,通过手机号查询用户
        User user = userMapper.findByPhone(phone);

        //5.判断用户是否注册过,若没有注册过,调用mapper注册一下
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);

            userMapper.save(user);
        }

        //6.返回user
        return R.success(user);
    }

    @Override
    public User login2(String phone, String code, String sessionCode) {
        //1. 校验验证码是否为空并且两个验证码是否一致
        User dbUser = null;
        if(sessionCode!=null && code.equalsIgnoreCase(sessionCode)){
            //验证码没有问题
            //2. 根据手机号查询用户，
            dbUser =  userMapper.findByPhone(phone);
            if(dbUser==null){
                //3. 如果为空代表该用户是新用户，直接注册
                dbUser = new User();
                dbUser.setPhone(phone);
                dbUser.setStatus(1);
                userMapper.save(dbUser);
            }
        }
        return dbUser;
    }
}
