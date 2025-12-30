package com.anyi.common.jdl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/25
 * @Copyright
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "jdl")
public class JdlProperties {

    private String appKey;
    private String appSecret;
    private String customerCode;
    private String accessToken;
    private String serverUrl;
}