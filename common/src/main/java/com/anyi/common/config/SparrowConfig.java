package com.anyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sparrow")
public class SparrowConfig {

    private String baseUrl;
    private String blackToken;

}
