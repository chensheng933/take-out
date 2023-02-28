package com.itheima.reggie.service;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;

public interface UserService {
    R<User> login(String phone, String requestCode, String sessionCode);

    User login2(String phone, String requestCode, String sessionCode);
}
