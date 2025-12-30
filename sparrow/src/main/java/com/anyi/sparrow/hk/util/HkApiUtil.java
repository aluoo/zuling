package com.anyi.sparrow.hk.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.common.utils.AesUtil;
import com.anyi.sparrow.hk.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.util.*;


/**
 * @author chenjian
 * @Description
 * @Date 2025/7/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class HkApiUtil {
    private static final String MCH_ID = "jihengzhuan";
    private static final String MCH_KEY = "417df7cd86df87d31ce955938839a756";
    private static final String AES_IV = "48c76d76ce032291";


    public static void main(String[] args) {
        ApplyOrderReq req = new ApplyOrderReq();
        req.setFetchCode("ZZGGD28250HHJqg");
        req.setThirdOrderSn("202507291537");
        req.setName("陈健");
        req.setIdCard("350721199101233219");
        req.setMobile("15260685717");
        req.setProvinceName("福建省");
        req.setCityName("厦门市");
        req.setTownName("集美区");
        req.setAddress("软件园三期B08栋13楼");
        ApplyOrderRsp resp = applyOrder(req,"1MkvVKKGewbasF+1eiFw4iH48Q5pQ32JLZnILjsEax1XRfBM3BBZHw3cN9g1FqlIJF7xtHydoDs/xG80Wlw6U89IOSJhAYm8b/l6ei74c+Mrrcn9u1tpTRIZbgO3J7ysjIJrhYmJWdHAL077CHnflJSA+cOafcIqr0tXdTfsQoo=");
        String aa = "21312312312";
        getToken();
    }

    public static ApplyOrderRsp applyOrder(ApplyOrderReq req, String token) {
        Map<String, Object> signMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        signMap.put("fetch_code",req.getFetchCode());
        signMap.put("third_order_sn",req.getThirdOrderSn());
        signMap.put("name",req.getName());
        signMap.put("id_card",req.getIdCard());
        signMap.put("mobile",req.getMobile());
        signMap.put("province_name",req.getProvinceName());
        signMap.put("city_name",req.getCityName());
        signMap.put("town_name",req.getTownName());
        signMap.put("address",req.getAddress());
        if(StrUtil.isNotBlank(req.getPlanMobileNumber())){
            signMap.put("plan_mobile_number",req.getPlanMobileNumber());
        }
        BeanUtil.copyProperties(signMap, bodyMap);
        bodyMap.put("name",AesUtil.encrypt(req.getName(),MCH_KEY,AES_IV));
        bodyMap.put("id_card",AesUtil.encrypt(req.getIdCard(),MCH_KEY,AES_IV));
        bodyMap.put("mobile",AesUtil.encrypt(req.getMobile(),MCH_KEY,AES_IV));
        if(StrUtil.isNotBlank(req.getPlanMobileNumber())){
            bodyMap.put("plan_mobile_number",AesUtil.encrypt(req.getPlanMobileNumber(),MCH_KEY,AES_IV));
        }
        HkCommonResp commonResp = execBase(bodyMap,signMap,token,"https://sysout.houjiantongxin.com/outapi/hk/submitOrder",Boolean.TRUE);
        ApplyOrderRsp response = JSONUtil.toBean((JSONObject) commonResp.getData(),ApplyOrderRsp.class);
        return response;
    }

    public static List<TaskNumberRsp> taskNumber(TaskNumberReq req,String token) {
        Map<String, Object> bizMap = new HashMap<>();
        bizMap.put("fetch_code",req.getFetchCode());
        bizMap.put("province_name",req.getProvinceName());
        bizMap.put("city_name",req.getCityName());
        HkCommonResp commonResp = execBase(bizMap,bizMap,token,"https://sysout.houjiantongxin.com/outapi/hk/taskNumber",Boolean.TRUE);
        List<TaskNumberRsp> list = JSONUtil.toList((JSONArray)commonResp.getData(),TaskNumberRsp.class);
        return list;
    }

    public static TokenRsp getToken() {
        Map<String, Object> bizMap = new HashMap<>();
        bizMap.put("mch_id",MCH_ID);
        bizMap.put("mch_key",MCH_KEY);
        HkCommonResp commonResp = execBase(bizMap,bizMap,"","https://sysout.houjiantongxin.com/outapi/hk/getToken",Boolean.FALSE);
        TokenRsp resp = JSONUtil.toBean((JSONObject) commonResp.getData(),TokenRsp.class);
        return resp;
    }

    private static HkCommonResp execBase(Map<String, Object> bodyMap,Map<String, Object> signMap,String headToken,String url,Boolean sign) {
        HttpRequest request = HttpUtil.createPost(url);
        request.contentType(ContentType.JSON.getValue());
        if(sign){
            request.header("token",headToken);
            request.header("mchId",MCH_ID);
            String signText = "";
            try {
                 signText = generateSign(signMap);
            } catch (Exception e) {
                throw new BusinessException(-1,"签名失败");
            }
            request.header("sign",signText);
            log.info("签名-{}", signText);
        }
        request.body(JSONUtil.toJsonStr(bodyMap));
        log.info("号卡请求地址-{}, 报文-{}, TOKEN-{}", url, JSONUtil.toJsonStr(bodyMap),headToken);
        try (HttpResponse response = request.execute()) {
            log.info("号卡响应报文-{}", JSONUtil.toJsonStr(response.body()));
            if (!response.isOk() || Objects.isNull(response.body())) {
                log.error("hk-api: error response {}", JSONUtil.toJsonStr(response));
                throw new BusinessException(-1, "hk api error");
            }
            HkCommonResp commonResp = JSONUtil.toBean(response.body(), HkCommonResp.class);
            if (commonResp.getCode() != 200) {
                log.error("hk-api: error response {}-{}", commonResp.getCode(), commonResp.getMsg());
                throw new BusinessException(-1, StrUtil.format("hk-api {}-{}", commonResp.getCode(), commonResp.getMsg()));
            }
            return commonResp;
        }
    }

    /**
     * 生成API请求签名
     * @param bizParams 业务参数键值对
     * @return 32位小写MD5签名
     */
    public static String generateSign(Map<String, Object> bizParams) throws Exception{
        // 1. 准备所有参与签名的参数
        TreeMap<String, Object> signParams = new TreeMap<>();
        // 业务参数
        if (bizParams != null) {
            signParams.putAll(bizParams);
        }
        // 2. 拼接参数字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : signParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).append("&");
        }
        // 移除最后一个多余的"&"字符
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        String stringA = sb.toString();
        log.info("签名原文-{}", stringA);
        // 4. 生成MD5签名,大写
        return SecureUtil.md5(stringA).toUpperCase();
    }
}