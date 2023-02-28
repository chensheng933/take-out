package com.itheima.reggie.common;

public interface ConstantUtil {
    /**
     * 初始化密码
     */
    String INIT_PASSWORD = "123456";

    /**
     * 状态:启动
     */
    Integer STATUS_ON = 1;

    /**
     * 状态:禁用
     */
    Integer STATUS_OFF = 0;

    /**
     * 客户登陆验证码:redis
     */
    String LOGINCODE="USER:CODE:";

    /**
     * 在售的菜品列表:redis
     */
    String DISH_ONSALE = "DISH:ONSALE:";
}
