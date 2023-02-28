package com.itheima.reggie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class MyRedisConfig {

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        //设置redis的链接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //只给key设置序列化器
        redisTemplate.setKeySerializer(RedisSerializer.string());

        return redisTemplate;
    }
}
