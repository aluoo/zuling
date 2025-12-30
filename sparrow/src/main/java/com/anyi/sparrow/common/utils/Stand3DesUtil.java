package com.anyi.sparrow.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Stand3DesUtil {

	private static final Logger logger = LoggerFactory.getLogger(Stand3DesUtil.class);

	/**
	 * 转换成十六进制字符串
	 * @param username
	 * @return
	 */
	public static byte[] hex(String key) {
		String f = DigestUtils.md5Hex(key);
		byte[] bkeys = f.getBytes(StandardCharsets.UTF_8);
		byte[] enk = new byte[24];
		System.arraycopy(bkeys, 0, enk, 0, 24);
		return enk;
	}

	/**
	 * 3DES加密
	 * @param key 密钥
	 * @param srcStr 将加密的字符串
	 * @return
	 */
	public static String encode3Des(String key, String srcStr) {

		try {
			byte[] src = srcStr.getBytes(StandardCharsets.UTF_8);
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "DESede");
			// 加密
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			// return c1.doFinal(src);//在单一方面的加密或解密
			return Base64.encodeBase64String(c1.doFinal(src));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	/**
	 * 3DES解密
	 * @param key 加密密钥
	 * @param desStr 解密后的字符串
	 * @return
	 */
	public static String decode3Des(String key, String desStr) {
		byte[] src ;
		try {
			src = Base64.decodeBase64(desStr.getBytes(StandardCharsets.UTF_8));
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "DESede");
			// 解密
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return new String(c1.doFinal(src), StandardCharsets.UTF_8);
		} catch (Exception e1) {
			logger.error(e1.getLocalizedMessage(), e1);
		}
		return null;
	}

	public static void main(String args[]) throws Exception{
		String str = "{\"channelId\":\"8362\",\"releaseType\":\"0\",\"vehicleColor\":\"0\",\"vehiclePlate\":\"贵E99999\",\"vehicleType\":\"1\"}\n";
		String s = encode3Des("gdetc2019xx29caa9a61a4bc", str);
		System.out.println(s);

	}
}


