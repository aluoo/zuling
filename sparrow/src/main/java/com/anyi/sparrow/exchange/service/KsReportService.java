package com.anyi.sparrow.exchange.service;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.oss.FileUploader;
import com.anyi.common.oss.OSSBucketEnum;
import com.anyi.sparrow.common.utils.HttpClientUtil;
import com.anyi.sparrow.exchange.req.KuaiShouReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.ObjectInput;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class KsReportService {

    public static final String app_id = "ks706220719228828444";
    public static final String app_key = "_UU-jmKk7yt672Q5qnRMuQ";

    public static final String access_Url = "https://open.kuaishou.com/oauth2/access_token";

    public static final String report_Url = "https://open.kuaishou.com/openapi/ground/tokenAttribution/infoUpload";

    public String getAccessToken() {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("app_id",app_id);
        reqMap.put("app_secret",app_key);
        reqMap.put("grant_type","client_credentials");
        String reportResult = HttpClientUtil.sendPost(access_Url,reqMap);

        JSONObject result = JSONObject.parseObject(HttpClientUtil.sendPost(access_Url,reqMap));
        String errorCode = result.getString("result");
        if (!StringUtils.isEmpty(errorCode) && !"1".equals(errorCode)) {
            log.info("快手上报请求AccessToken失败{}", reportResult);
            throw new BusinessException(99999,"快手上报请求AccessToken失败");
        }
        return result.getString("access_token");
    }

    public void report(KuaiShouReq req) {

        String accessToken = getAccessToken();

        String url = report_Url + "?app_id=" +app_id + "&access_token="+accessToken;

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(JSON.toJSON(req));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businessType", 1);
        jsonObject.put("appType", 1);
        jsonObject.put("attributionInfos", jsonArray);

        log.info("请求参数{}:{}",url,JSON.toJSONString(jsonObject));

        String result = HttpClientUtil.sendPostJson(url,jsonObject);

        log.info("返回参数"+result);
    }
}