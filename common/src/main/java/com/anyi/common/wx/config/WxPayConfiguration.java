package com.anyi.common.wx.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jianpan
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WxPayConfiguration {
  private WxPayProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public WxPayService wxService() {

    List<WxPayProperties.Config> configs = this.properties.getConfigs();
    if (configs == null) {
      throw new WxRuntimeException("添加下相关配置，注意别配错了！");
    }

    Map<String,WxPayConfig> wxPayConfigMap = configs.stream().map(config -> {

      WxPayConfig payConfig = new WxPayConfig();
      payConfig.setAppId(StringUtils.trimToNull(config.getAppId()));
      payConfig.setMchId(StringUtils.trimToNull(config.getMchId()));
      payConfig.setMchKey(StringUtils.trimToNull(config.getMchKey()));
      //子商户
      payConfig.setSubAppId(StringUtils.trimToNull(config.getSubAppId()));
      payConfig.setSubMchId(StringUtils.trimToNull(config.getSubMchId()));

      payConfig.setKeyPath(StringUtils.trimToNull(config.getKeyPath()));
      payConfig.setNotifyUrl(StringUtils.trimToNull(config.getNotifyUrl()));
      //以下是v3
      payConfig.setApiV3Key(StringUtils.trimToNull(config.getApiV3Key()));
      payConfig.setCertSerialNo(StringUtils.trimToNull(config.getCertSerialNo()));
      payConfig.setPrivateKeyPath(StringUtils.trimToNull(config.getPrivateKeyPath()));
      payConfig.setPrivateCertPath(StringUtils.trimToNull(config.getPrivateCertPath()));

      // 可以指定是否使用沙箱环境
      payConfig.setUseSandboxEnv(false);

      return payConfig;

    }).collect(Collectors.toMap(WxPayConfig::getSubMchId, a -> a, (k1, k2) -> k1));

    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setMultiConfig(wxPayConfigMap);
    return wxPayService;
  }

}