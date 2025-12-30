package com.anyi.common.qfu;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/21
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties({QfuProperties.class, QfuAlipayProperties.class})
public class QfuConfig {
    private QfuProperties properties;
    private QfuAlipayProperties alipayProperties;

    @Bean("qfuApi")
    public QfuApi qfuApi() throws Exception {
        PrivateKey privateKey = Utils.readPrivateKey(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties.getClientKeyPath()));
        PublicKey publicKey = Utils.readPublicKey(Thread.currentThread().getContextClassLoader().getResourceAsStream(properties.getServerKeyPath()));
        Credentials credentials = new Credentials(privateKey, publicKey, properties.getVendorId(), properties.getAgencyId());
        return new QfuApi(credentials);
    }

    @Bean("qfuAlipayApi")
    public QfuApi qfuAlipayApi() throws Exception {
        PrivateKey privateKey = Utils.readPrivateKey(Thread.currentThread().getContextClassLoader().getResourceAsStream(alipayProperties.getClientKeyPath()));
        PublicKey publicKey = Utils.readPublicKey(Thread.currentThread().getContextClassLoader().getResourceAsStream(alipayProperties.getServerKeyPath()));
        Credentials credentials = new Credentials(privateKey, publicKey, alipayProperties.getVendorId(), alipayProperties.getAgencyId());
        return new QfuApi(credentials);
    }
}