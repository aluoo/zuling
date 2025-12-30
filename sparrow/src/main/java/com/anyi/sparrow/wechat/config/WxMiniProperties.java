package com.anyi.sparrow.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author peng can
 * @date 2022/12/1 21:45
 * 微信小程序配置
 */
@ConfigurationProperties(prefix = "wechat.mini")
@Data
public class WxMiniProperties {

    private String appId;

    private String appSecret;

    /**
     * 公众号推展示卡片
     */
    private String title;

    /**
     * 公众号跳转地址
     */
    private String pagePath;

}
