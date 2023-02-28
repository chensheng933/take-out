package com.itheima.reggie.converter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyString2DateConverter implements Converter<String,Date> {
    @Override
    public Date convert(String s) {
        if (StrUtil.isNotBlank(s)) {
            return DateUtil.parse(s,"yyyy-MM-dd");
        }
        return null;
    }
}
