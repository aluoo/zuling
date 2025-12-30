package com.anyi.sparrow.cyx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@ConfigurationProperties(prefix = "anyicx.api-sign.cyx")
@Configuration
@Data
public class ApiSignConfig {

    private String appId;
    private String secretKey;
}