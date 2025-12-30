package com.anyi.sparrow.alipay.config;

import com.alipay.api.AlipayConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付配置
 *
 * @author chenjian
 */
@Configuration
@ConfigurationProperties(prefix = "alipay.anyi")
@Data
public class AlipayProperties {

    private String privateKey;

    /**
     * 设置应用公钥证书路径
     */
    private String appCertPath;

    /**
     * 设置支付宝公钥证书路径
     */
    private String alipayCertPath;
    /**
     * 设置支付宝根证书路径
     */
    private String alipayRootCertPath;

    private String appId;

    private String serverUrl;

}
