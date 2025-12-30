package com.anyi.sparrow.cyx.util;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.anyi.sparrow.cyx.constant.SignInfoConstants;
import com.google.common.collect.Maps;
import com.anyi.sparrow.cyx.req.ActiveReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ：shenbinhong
 * @description：第三方接口 sha256 数字签名工具类
 */
@Slf4j
public class SignatureUtils {

    /**
     * sha256验证签名
     *
     * @param headerSignInfo 第三方传递过来的验证签名的参数,包括签名、签名方式、访问id、时间戳等
     * @param bizParam       接口的业务参数
     * @param secretKey      访问id对应的秘钥
     * @param serviceCode    服务编码
     * @return 状态码
     */
    public static boolean sha256checkInterfaceSignature(Map<String, Object> headerSignInfo, Map<String, Object> bizParam, String secretKey, String serviceCode) {


        // 第三方传入的签名
        String signature = (String) headerSignInfo.getOrDefault("sign", "");
        // 在map用标签替换签名


        // 生成加密签名
        String mySignature = generateSha256Sign(headerSignInfo, bizParam, secretKey, serviceCode);

        log.info(" sign : {}", mySignature);

        // 校验签名是否一致
        if (StringUtils.isNotBlank(mySignature) && mySignature.equals(signature)) {
            return true;
        }
        return false;
    }


    /**
     * 生成sha256签名
     *
     * @param headerSignInfo 三方传递过来的验证签名的参数,包括签名、签名方式、访问id、时间戳等
     * @param bizParam       接口的业务参数
     * @param secretKey      访问id对应的秘钥
     * @param serviceCode    服务编码
     * @return
     */
    public static String generateSha256Sign(Map<String, Object> headerSignInfo, Map<String, Object> bizParam, String secretKey, String serviceCode) {

        // 对业务参数key排序
        String[] bizKeys = bizParam.keySet().toArray(new String[0]);
        sort(bizKeys);

        Map<String, Object> bizContentMap = new LinkedHashMap<>();
        for (int i = 0; i < bizKeys.length; i++) {
            // 去除不为null的参数
            if (bizParam.get(bizKeys[i]) != null) {
                bizContentMap.put(bizKeys[i], bizParam.get(bizKeys[i]));
            }
        }

        Map<String, Object> tempHeaderSignInfo = Maps.newHashMap();
        tempHeaderSignInfo.putAll(headerSignInfo);
        tempHeaderSignInfo.put("biz_content", JSONObject.toJSONString(bizContentMap));

        log.info("biz_content {}", JSONObject.toJSONString(bizContentMap));

        // 对参数进行排序
        Set<String> keys = tempHeaderSignInfo.keySet();
        keys.remove("sign");
        String[] signatureParamsKeys = keys.toArray(new String[0]);
        sort(signatureParamsKeys);

        // 组装参与签名生成的字符串(使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串)
        StringBuilder waitSignBuider = new StringBuilder();
        waitSignBuider.append(serviceCode).append("?");
        for (int i = 0; i < signatureParamsKeys.length; i++) {
            // 拼接公共参数
            //拼接随机字符串、时间戳参数
            String paramValue = (null != tempHeaderSignInfo.get(signatureParamsKeys[i]) ? tempHeaderSignInfo.get(signatureParamsKeys[i]).toString() : "");
            waitSignBuider.append(signatureParamsKeys[i]).append("=").append(paramValue);

            if (i != signatureParamsKeys.length - 1) {
                waitSignBuider.append("&");
            }
        }

        waitSignBuider.append(secretKey);
        log.info("wait sign string: {}", waitSignBuider.toString());

        // 生成加密签名
        String finalSign = SHA256(waitSignBuider.toString()).toUpperCase();

        log.info(" sign : {}", finalSign);

        return finalSign;
    }

    public static void main(String[] args) {
        Map<String, Object> headerSignInfo = Maps.newHashMap();
//        headerSignInfo.put("appid", "2014072300007148");
        headerSignInfo.put("appid", SignInfoConstants.CYX_APP_ID);
        headerSignInfo.put("signtype", "sha256");
//        headerSignInfo.put("sign", "E30098E052D17BD15E9FA5E71EC9CD8396D4DE44FA6F41EAE8388CF6029A1340");
        headerSignInfo.put("timestamp", System.currentTimeMillis()+"");
//        headerSignInfo.put("nonceStr", System.currentTimeMillis()+"");

        log.info("原始headerSignInfo {}", JSONObject.toJSONString(headerSignInfo));


        /*Map<String, Object> bizParam = Maps.newHashMap();

        bizParam.put("orderNo", "1468902291884130305");*/
        ActiveReq activeReq = ActiveReq.builder()
                .activeTime("2023-03-20T11:13:39")
                .cardId("99010041232100000078")
                .driverPhone("15239015559")
                .obuId("9901000300024321")
                .orderNo("1468902291884130305")
                .ownerName("范宣明")
                .plateColor("0")
                .plateNum("豫YYC114")
                .build();
        Map<String, Object> bizParam = BeanUtil.beanToMap(activeReq, false, true);


        log.info("bizParam {}", JSONObject.toJSONString(bizParam));

//        String secretKey = "sdfdfdfdfdfdf";
        String secretKey =  SignInfoConstants.CYX_APP_SECRETKEY;
        // String serviceCode = "/api/cyx/queryOrderStatus";
        String serviceCode = "/api/cyx/activeCallback";
//        String serviceCode = "/api/v1/devices";

//        String sign = generateSha256Sign(headerSignInfo, bizParam, secretKey, serviceCode);
        String sign = generateSha256Sign(headerSignInfo, bizParam, secretKey, serviceCode);
        System.out.println("sign===="+sign);

        headerSignInfo.put("sign",sign);
        log.info("加入签名后的headerSignInfo {}", JSONObject.toJSONString(headerSignInfo));
    }


    /**
     * 字典排序算法
     *
     * @param strArr 待排序数组
     */
    public static void sort(String[] strArr) {
        for (int i = 0; i < strArr.length - 1; i++) {
            for (int j = i + 1; j < strArr.length; j++) {
                if (strArr[j].compareTo(strArr[i]) < 0) {
                    String temp = strArr[i];
                    strArr[i] = strArr[j];
                    strArr[j] = temp;
                }
            }
        }
    }

    /**
     * 字符串 SHA 加密
     *
     * @return
     */
    private static String SHA256(String strText) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }
}