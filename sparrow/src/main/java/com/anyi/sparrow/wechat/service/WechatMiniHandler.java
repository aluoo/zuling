package com.anyi.sparrow.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.common.utils.HttpClientUtil;
import com.anyi.sparrow.common.utils.WxUtils;
import com.anyi.sparrow.common.vo.WxOpenId;
import com.anyi.sparrow.common.vo.WxPhoneInfo;
import com.anyi.sparrow.wechat.config.WxMiniProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peng can
 * @date 2022/12/3 15:51
 * 小程序处理类
 */
@Component
@Slf4j
public class WechatMiniHandler {

    @Autowired
    private WxUtils wxUtils;

    @Autowired
    private WxMiniProperties wxMiniProperties;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WxTokenService wxTokenService;

    private final static String WX_URL = "https://api.weixin.qq.com/cgi-bin/";

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private final static String WX_URL_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";

    private final static String WX_LINK_URL = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=";

    private final static String WX_MOBILE_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";

    public WxOpenId getOpenId(String authCode) {

        Map<String, String> params = new HashMap<>();
        params.put("appid", wxMiniProperties.getAppId());
        params.put("secret", wxMiniProperties.getAppSecret());
        params.put("js_code", authCode);
        params.put("grant_type", "authorization_code");
        JSONObject result = JSONObject.parseObject(HttpClientUtil.sendGet(WX_LOGIN_URL, params));
        String errorCode = result.getString("errcode");
        if (StringUtils.isNotBlank(errorCode) && !"0".equals(errorCode)) {
            log.error("获取openid, authCode:{}, errorCode:{},errorMsg:{}", authCode, errorCode, result.getString("errmsg"));
            throw new BusinessException(BizError.GET_OPENID_ERROR);
        }
        WxOpenId wxOpenId = new WxOpenId();
        wxOpenId.setSessionKey(result.getString("session_key"));
        wxOpenId.setOpenId(result.getString("openid"));
        wxOpenId.setUnionId(result.getString("unionid"));
        return wxOpenId;
    }

    public WxPhoneInfo getWxPhone(String sessionKey, String encryptedData, String iv) {

        return wxUtils.getWxPhone(sessionKey, encryptedData, iv);
    }

    public String getPhoneNumber(String authCode) {

        JSONObject param = new JSONObject();
        param.put("code", authCode);
        String accessToken = getAccessToken();
        log.info("获取微信手机号码TOKEN:{}", accessToken);
        byte[] bytes = HttpClientUtil.sendPost(WX_MOBILE_URL + accessToken, param);
        JSONObject jsonObject = JSON.parseObject(new String(bytes));
        log.info("获取微信手机号码返回:{}", JSON.toJSONString(jsonObject));
        if (!jsonObject.getInteger("errcode").equals(0)) {
            throw new BusinessException(BizError.MINI_MOBILE);
        }
        return jsonObject.getJSONObject("phone_info").getString("purePhoneNumber");
    }

    /**
     * 获取微信access_token
     *
     * @return
     */
    public String getAccessToken() {
        return wxTokenService.getMiniAccessToken();
    }
}
