package com.anyi.sparrow.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.anyi.common.req.PageReq;
import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class DESUtils {
 
    private static final String DES_NAME = "DES";
    private static final String CHARSET = "UTF-8";
    
    private DESUtils() {
    }
 
    // ===============================加密 begin
 
    public static byte[] encrypt(String source, String key) throws Exception {
        return encrypt(source.getBytes(CHARSET), key);
    }
 
    public static byte[] encrypt(byte[] source, String key) throws Exception {
        return encryptOrDecrypt(Cipher.ENCRYPT_MODE, source, key);
    }
 
    public static String encryptToHexString(byte[] source, String key) throws Exception {
        byte[] encodeByte = encrypt(source, key);
        return HexUtils.toHexString(encodeByte);
    }
 
    public static String encryptToHexString(String source, String key) throws Exception {
        byte[] encodeByte = encrypt(source, key);
        return HexUtils.toHexString(encodeByte);
    }
 
    public static String encryptToBase64String(String source, String key) throws Exception {
        byte[] encodeByte = encrypt(source, key);
        return Base64.getEncoder().encodeToString(encodeByte);
    }
    // ===============================加密 end
 
    // ===============================解密 begin
    /**
     * 解密
     * @param source
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] source, String key) throws Exception {
        return encryptOrDecrypt(Cipher.DECRYPT_MODE, source, key);
    }
 
    /**
     *
     * @param source 16进制
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptHex(String source, String key) throws Exception {
        return decrypt(HexUtils.fromHexString(source), key);
    }
 
    public static byte[] decryptBase64(String source, String key) throws Exception {
        return decrypt(Base64.getDecoder().decode(source), key);
    }
 
    public static String decryptToString(byte[] source, String key) throws Exception {
        byte[] decodeByte = decrypt(source, key);
        return new String(decodeByte, CHARSET);
    }
 
    /**
     * @param source 16进制
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptHexToString(String source, String key) throws Exception {
        byte[] decodeByte = decryptHex(source, key);
        return new String(decodeByte, CHARSET);
    }
 
    public static String decryptBase64ToString(String source, String key) throws Exception {
        byte[] decodeByte = decryptBase64(source, key);
        return new String(decodeByte, CHARSET);
    }
 
    // ===============================解密 end
 
    private static byte[] encryptOrDecrypt(int mode, byte[] byteContent, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_NAME);//返回实现指定转换的 Cipher 对象
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES_NAME);
        // 用密匙初始化Cipher对象
        cipher.init(mode, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(byteContent);
    }

    public static void main(String[] args) throws Exception{
        String encrypt = encryptToBase64String(JSONObject.toJSONString(new PageReq()), "zfadagadgfaga");
        System.out.println(encrypt);
        System.out.println(decryptBase64ToString(encrypt, "zfadagadgfaga"));
    }
}