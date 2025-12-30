package com.anyi.common.config;


import com.anyi.common.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置
 *
 * @author shenbh 2023-10-12
 */
@Configuration
@Slf4j
public class SnowflakeIdConfig {

    @Value("${snowflakeid.workerId}")
    private Long workerId;

    @Value("${snowflakeid.datacenterId}")
    private Long datacenterId;

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        log.info(">>>>>>>>>>> snowflakeIdWorker....workerId：{} ....datacenterId: {} .... ", workerId, datacenterId);
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(workerId, datacenterId);
        return snowflakeIdWorker;
    }




}