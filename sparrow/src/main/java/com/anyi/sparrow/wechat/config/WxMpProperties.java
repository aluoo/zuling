package com.anyi.sparrow.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author peng can
 * @date 2022/12/1 21:45
 * 微信公众号配置
 */
@ConfigurationProperties(prefix = "wechat.mp")
@Data
public class WxMpProperties {

    private String appId;

    private String appSecret;

    private String aesKey;

    private String token;

}
