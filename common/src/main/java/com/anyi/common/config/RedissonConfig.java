package com.anyi.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/7/12
 */
@Configuration
public class RedissonConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient getClient() {

        Config config = new Config();
        config.useSingleServer().setConnectionMinimumIdleSize(10);
        config.useSingleServer().setAddress("redis://"+redisProperties.getHost()+":"+redisProperties.getPort())
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword())
                // 设置1秒钟ping一次来维持连接
                .setPingConnectionInterval(1000)
        ;
        RedissonClient redisson = Redisson.create(config);
        return redisson;


    }
}