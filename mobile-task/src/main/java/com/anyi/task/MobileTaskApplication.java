package com.anyi.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/28
 * @Copyright
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.anyi"})
@MapperScan(basePackages = {"com.anyi.**.mapper"})
@EnableScheduling
@EnableAsync
public class MobileTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileTaskApplication.class, args);
    }
}