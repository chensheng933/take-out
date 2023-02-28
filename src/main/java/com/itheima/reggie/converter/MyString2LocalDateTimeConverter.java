package com.itheima.reggie.converter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyString2LocalDateTimeConverter implements Converter<String,LocalDateTime> {
    @Override
    public LocalDateTime convert(String s) {
        if (StrUtil.isNotBlank(s)) {
            return DateUtil.parseLocalDateTime(s,"yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }
}
