package com.zulong.web.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.data.redis.cache.RedisCacheManager;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    //创建 RedisConnectionFactory Bean，以便 Spring Cache 连接 Redis
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName("localhost");
        connectionFactory.setPort(6379);
        return connectionFactory;
    }

    //创建 CacheManager Bean，使用 Redis 作为 Spring Cache 管理器
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = RedisCacheManager.create(redisConnectionFactory());
        return cacheManager;
    }

    //配置 Key 生成器，生成缓存 Key
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}