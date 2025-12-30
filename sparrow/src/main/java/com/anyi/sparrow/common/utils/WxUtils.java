package com.anyi.sparrow.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.common.vo.WxOpenId;
import com.anyi.sparrow.common.vo.WxPhoneInfo;
import com.anyi.sparrow.wechat.service.WxTokenService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class WxUtils {
    @Value("${wx.appId:}")
    private String wxAppId = "wx166b3ef309bd037c";

    @Value("${wx.secret:}")
    private String wxSecret = "5b09ae1f420e65e2b51600b96d162b1d";

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    private FileUploader fileUploader;

    @Autowired
    private WxTokenService wxTokenService;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    private String tmpdir = System.getProperty("java.io.tmpdir");

    private final static String WX_URL = "https://api.weixin.qq.com/cgi-bin/";

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private final static String WX_URL_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";

    private final static String WX_LINK_URL = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=";

    private final static String WX_MOBILE_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=";

    private static Logger logger = LoggerFactory.getLogger(WxUtils.class);

    public String getWxAppId() {
        return wxAppId;
    }

    public String getWxSecret() {
        return wxSecret;
    }

    /**
     * 获取微信access_token
     *
     * @return
     */
    public String getToken() {
        /*Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", getWxAppId());
        params.put("secret", getWxSecret());
        String result = HttpClientUtil.sendGet(WX_URL + "token", params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("access_token");*/
        return wxTokenService.getMiniAccessToken();
    }

    public String getPhoneNumber(String authCode) {

        JSONObject param = new JSONObject();
        param.put("code", authCode);
        byte[] bytes = HttpClientUtil.sendPost(WX_MOBILE_URL + getToken(), param);
        JSONObject jsonObject = JSON.parseObject(new String(bytes));
        if (!jsonObject.getInteger("errcode").equals(0)) {
            throw new BusinessException(BizError.MINI_MOBILE);
        }
        return jsonObject.getJSONObject("phone_info").getString("purePhoneNumber");
    }

    public static void main(String[] args) {
        String code = "4818cb9ba3ceb884ce4a10ee238bd776f1c349151e4ed1ec07a835dda6b38a86";
        WxUtils wxUtils = new WxUtils();
        String token = wxUtils.getToken();
        System.out.println(token);
        //String token = "63_Ox5mDCMXzd71SDsQl4k45W54dgURuoWtGeItCU_t3E7rO85i8-yoWkV2CfZL63jne7x9bjabeuapWM1-Xt3Z2GA2kWs0t9JdVWwpLdM9zpEgeWCHzJwHAXPHJqgEIXbAGAZDS";
        String url = WX_MOBILE_URL + token;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        byte[] bytes = HttpClientUtil.sendPost(url, jsonObject);
        String s = new String(bytes);
        System.out.println(s);

    }

    /**
     * 生成小程序二维码
     *
     * @param params
     * @param width
     * @return
     */
    public String genQrCode(Map<String, String> params, int width) {
        return genQrCodeA(params, width);
    }

    /**
     * 生成跳转指定页面的二维码
     *
     * @param path
     * @param params
     * @param width
     * @return
     */
    public String genQrCode(String path, Map<String, String> params, int width) {
        return genQrCodeA(path, params, width);
    }

    public String genQrCodeA(String path, Map<String, String> params, int width) {
        JSONObject req = new JSONObject();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        req.put("path", path + sb.toString());
        req.put("width", String.valueOf(width));
        //正式
        String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode" + "?access_token=" + getToken();
        if (!"production".equals(env)) {
            //测试体验版
            url = "https://api.weixin.qq.com/wxa/getwxacode" + "?access_token=" + getToken();
            req.put("env_version", "trial");
        }
        byte[] bytes = HttpClientUtil.sendPost(url, req);
        String fileName = snowflakeIdService.nextId() + ".jpeg";
        File imgFile = new File(tmpdir + File.separator + fileName);
        try {
            FileUtils.writeByteArrayToFile(imgFile, bytes);
            String imgUrl = fileUploader.upload(imgFile, fileName, "aycx-wechat");
            return imgUrl;
        } catch (Exception e) {
            throw new BusinessException(BizError.GEN_QRCODE_ERROR);
        } finally {
            imgFile.delete();
        }
    }

    public String genQrCodeA(Map<String, String> params, int width) {
        JSONObject req = new JSONObject();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        req.put("path", "pages/Home/Home?" + sb.toString());
        req.put("width", String.valueOf(width));
        byte[] bytes = HttpClientUtil.sendPost("https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode" + "?access_token=" + getToken(), req);
        String fileName = snowflakeIdService.nextId() + ".jpeg";
        File imgFile = new File(tmpdir + File.separator + fileName);
        try {
            FileUtils.writeByteArrayToFile(imgFile, bytes);
            String imgUrl = fileUploader.upload(imgFile, fileName, "zylh-wxqr");
            return imgUrl;
        } catch (Exception e) {
            throw new BusinessException(BizError.GEN_QRCODE_ERROR);
        } finally {
            imgFile.delete();
        }
    }

    public String genQrCodeB(Map<String, String> params, int width) {
        JSONObject req = new JSONObject();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        req.put("scene", sb.toString());
        req.put("width", String.valueOf(width));
        byte[] bytes = HttpClientUtil.sendPost(WX_URL_QRCODE + "?access_token=" + getToken(), req);
        String fileName = snowflakeIdService.nextId() + ".jpeg";
        File imgFile = new File(tmpdir + File.separator + fileName);
        try {
            FileUtils.writeByteArrayToFile(imgFile, bytes);
            String imgUrl = fileUploader.upload(imgFile, fileName, "zylh-wxqr");
            return imgUrl;
        } catch (Exception e) {
            throw new BusinessException(BizError.GEN_QRCODE_ERROR);
        } finally {
            imgFile.delete();
        }
    }

    /**
     * 获取微信openid
     *
     * @param authCode
     * @return
     */
    public WxOpenId getOpenId(String authCode) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", getWxAppId());
        params.put("secret", getWxSecret());
        params.put("js_code", authCode);
        params.put("grant_type", "authorization_code");
        JSONObject result = JSONObject.parseObject(HttpClientUtil.sendGet(WX_LOGIN_URL, params));
        String errorCode = result.getString("errcode");
        if (!StringUtils.isEmpty(errorCode) && !"0".equals(errorCode)) {
            logger.error("获取openid, authCode:{}, errorCode:{},errorMsg:{}", authCode, errorCode, result.getString("errmsg"));
            throw new BusinessException(BizError.GET_OPENID_ERROR);
        }
        WxOpenId wxOpenId = new WxOpenId();
        wxOpenId.setSessionKey(result.getString("session_key"));
        wxOpenId.setOpenId(result.getString("openid"));
        wxOpenId.setUnionId(result.getString("unionid"));
        return wxOpenId;
    }

    /**
     * 获取微信手机信息
     *
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public WxPhoneInfo getWxPhone(String sessionKey, String encryptedData, String iv) {
        logger.info("解析手机号，sessionKey:{},encryptedData:{},iv:{}", sessionKey, encryptedData, iv);
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] encrypetedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        byte[] rBytes = AesUtil.decrypt(encrypetedBytes, sessionKeyBytes, ivBytes);
        JSONObject json = JSONObject.parseObject(new String(rBytes));
        WxPhoneInfo phoneInfo = new WxPhoneInfo();
        phoneInfo.setPhoneNumber(json.getString("phoneNumber"));
        phoneInfo.setPurePhoneNumber(json.getString("purePhoneNumber"));
        return phoneInfo;
    }
}
