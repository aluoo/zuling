package com.anyi.sparrow.wechat.config;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.anyi.sparrow.base.security.Constants;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@Slf4j
public class WxMpConfiguration {

    private final WxMpProperties wxMpProperties;

    private final StringRedisTemplate  stringRedisTemplate;

    @Value("${wx.accessToken.url}")
    private String tokenUrl;

    public WxMpConfiguration(WxMpProperties wxMpProperties, StringRedisTemplate stringRedisTemplate) {
        this.wxMpProperties = wxMpProperties;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    public WxMpConfigStorage wxMpConfigStorage() {
        RedisTemplateWxRedisOps redisOps = new RedisTemplateWxRedisOps(stringRedisTemplate);

        WxMpRedisConfigImpl wxMpRedisConfig = new WxMpRedisConfigImpl(redisOps, Constants.MP_REDIS_PREFIX){
            @Override
            public boolean isAccessTokenExpired() {
                return false;
            }

            @Override
            public String getAccessToken() {
                String result = HttpUtil.get(tokenUrl+"/wx/gzh/getAccessToken");
                log.info("调用微信公众号ACCESSTOKEN中控返回的结果{}",result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                String code = jsonObject.getString("code");
                String data = jsonObject.getString("data");
                return data;
            }


        };
        wxMpRedisConfig.setAppId(wxMpProperties.getAppId());
        wxMpRedisConfig.setSecret(wxMpProperties.getAppSecret());
        wxMpRedisConfig.setAesKey(wxMpProperties.getAesKey());
        wxMpRedisConfig.setToken(wxMpProperties.getToken());

        return wxMpRedisConfig;
    }

}