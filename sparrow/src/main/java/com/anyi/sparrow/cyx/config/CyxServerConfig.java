package com.anyi.sparrow.cyx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@ConfigurationProperties(prefix = "anyicx.highway.cyx")
@Configuration
@Data
public class CyxServerConfig {

    private String baseUrl;
    private String appId;
    private String aesKeyPrivate;
    private String rsaKeyPrivate;
    private Integer timeout;
    private boolean mock = false;
}