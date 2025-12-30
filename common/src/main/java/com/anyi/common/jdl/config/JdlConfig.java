package com.anyi.common.jdl.config;

import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.plugin.factory.OAuth2PluginFactory;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/25
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(JdlProperties.class)
public class JdlConfig {
    private JdlProperties properties;

    @Bean("jdlDomainApiClient")
    public DefaultDomainApiClient defaultDomainApiClient() {
        return new DefaultDomainApiClient(this.properties.getServerUrl(),500,15000);
    }

    @Bean
    public LopPlugin produceLopPlugin() {
        return OAuth2PluginFactory.produceLopPlugin(this.properties.getAppKey(), this.properties.getAppSecret(), this.properties.getAccessToken());
    }
}