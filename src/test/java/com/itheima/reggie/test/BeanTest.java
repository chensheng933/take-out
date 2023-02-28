package com.itheima.reggie.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeanTest {

    @Autowired
    Person person;

    @Test
    public void test0() {
        System.out.println(person);
    }
}
