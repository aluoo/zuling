package com.anyi.sparrow.wechat.service;

import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.base.security.Constants;
import com.anyi.sparrow.common.utils.HttpClientUtil;
import com.anyi.sparrow.wechat.config.WxMiniProperties;
import com.anyi.sparrow.wechat.config.WxMpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WxTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private WxMiniProperties wxMiniProperties;
    @Autowired
    private WxMpProperties wxMpProperties;

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${wx.accessToken.url}")
    private String tokenUrl;

    private final static String WX_URL = "https://api.weixin.qq.com/cgi-bin/";

    /**
     * 获取小程序access_token
     *
     * @return
     */
    public String getMiniAccessToken() {
        //生产环境直接获取
        if (env.equals("production")) {
            log.info("进入了请求微信地址获取TOKEN流程");
            String accessToken = redisTemplate.opsForValue().get(Constants.MINI_REDIS_PREFIX);
            if (StringUtils.isBlank(accessToken)) {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "client_credential");
                params.put("appid", wxMiniProperties.getAppId());
                params.put("secret", wxMiniProperties.getAppSecret());
                String result = HttpClientUtil.sendGet(WX_URL + "token", params);
                JSONObject jsonObject = JSONObject.parseObject(result);
                accessToken = jsonObject.getString("access_token");
                Integer expiresIn = jsonObject.getInteger("expires_in");
                redisTemplate.opsForValue().set(Constants.MINI_REDIS_PREFIX, accessToken, expiresIn - 200, TimeUnit.SECONDS);
            }
            return accessToken;
        }
        //非生产统一调生产的获取
        String accessToken = "";
        try {
            log.info("获取小程序AccessToken{}走请求生产地址是{}", tokenUrl);
            String result = HttpUtil.get(tokenUrl + "/wx/mini/getAccessToken");
            log.info("获取小程序AccessToken走请求生产,返回结果是{}", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code = jsonObject.getString("code");
            if (!String.valueOf(HttpStatus.HTTP_OK).equals(code)) {
                throw new BusinessException(BizError.WX_TOKEN_ERROR);
            }
            accessToken = jsonObject.getString("data");
        } catch (Exception e) {
            throw new BusinessException(BizError.WX_TOKEN_ERROR);
        }
        return accessToken;
    }


    /**
     * 获取公众号access_token
     *
     * @return
     */
    public String getGzhAccessToken() {
        //生产环境直接获取
        if (env.equals("production")) {
            String accessToken = redisTemplate.opsForValue().get(Constants.GZH_REDIS_PREFIX);
            if (StringUtils.isBlank(accessToken)) {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "client_credential");
                params.put("appid", wxMpProperties.getAppId());
                params.put("secret", wxMpProperties.getAppSecret());
                String result = HttpClientUtil.sendGet(WX_URL + "token", params);
                JSONObject jsonObject = JSONObject.parseObject(result);
                accessToken = jsonObject.getString("access_token");
                Integer expiresIn = jsonObject.getInteger("expires_in");
                redisTemplate.opsForValue().set(Constants.GZH_REDIS_PREFIX, accessToken, expiresIn - 200, TimeUnit.SECONDS);
            }
            return accessToken;
        }
        //非生产统一调生产的获取
        String accessToken = "";
        try {
            log.info("获取公众号AccessToken{}走请求生产地址是{}", tokenUrl);
            String result = HttpUtil.get(tokenUrl + "/wx/gzh/getAccessToken");
            log.info("获取公众号AccessToken走请求生产,返回结果是{}", result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String code = jsonObject.getString("code");
            if (!String.valueOf(HttpStatus.HTTP_OK).equals(code)) {
                throw new BusinessException(BizError.WX_TOKEN_ERROR);
            }
            accessToken = jsonObject.getString("data");
        } catch (Exception e) {
            throw new BusinessException(BizError.WX_TOKEN_ERROR);
        }
        return accessToken;
    }

}