package com.itheima.reggie.test;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class IdTest {
    @Test
    public void test0() {
        long i1 = IdUtil.getSnowflake().nextId();
        long i2 = IdUtil.getSnowflake().nextId();
        long i3 = IdUtil.getSnowflake().nextId();

        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
        System.out.println("-------");
        long i11 = IdUtil.getSnowflake(11).nextId();
        long i21 = IdUtil.getSnowflake(11).nextId();
        long i31 = IdUtil.getSnowflake(11).nextId();

        System.out.println(i11);
        System.out.println(i21);
        System.out.println(i31);
    }

    @Test
    public void test01() {
        BigDecimal i1 = new BigDecimal("1.1");
        BigDecimal j1 = new BigDecimal("1.1");

        System.out.println(i1.multiply(j1));

       /* double i=1.1,j=3;
        System.out.println(i*j);*/


    }

}
