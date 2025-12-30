package com.anyi.sparrow.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);
    public static boolean verify(String data, String sign, String key){
        logger.info("待签名字符串{}，param sign,{}", data + key, sign);
        String s = DigestUtils.md5Hex(data + key);
        logger.info("签名结果:{}", s);
        return s.equals(sign);
    }

    public static String sign(String data, String key){
        return DigestUtils.md5Hex(data + key);
    }
}
