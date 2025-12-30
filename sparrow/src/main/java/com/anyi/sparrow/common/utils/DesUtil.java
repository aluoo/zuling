package com.anyi.sparrow.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @ClassName: DesUtil
 * @Description: 渠道使用,针对zip=0且keyType=1处理,请求时，注意对参数进行urlEncode处理，避免加号传递无效
 */
public class DesUtil {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

	private static final char[] SIXTY_FOUR_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	/**
	 * des加密，返回string类型，指定编码集
	 * 
	 * @time 2018-11-6 16:08:27
	 * @author Lxm
	 * @param data
	 * @param charset
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encode(String data, Charset charset, String key)
			throws Exception {

		byte[] bt = encode(key, data.getBytes(charset));

		String encode = encode641(bt);
		return encode.replaceAll("[\r\n]", "");//未对参数urlEncode处理，可使用替换处理.replaceAll("\\+","%2B");
	}
	
	/**
	 * DES算法，加密
	 *
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws CryptException
	 *             异常
	 */
	public static byte[] encode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

			byte[] bytes = cipher.doFinal(data);
			return bytes;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static String encode641(byte[] input) {
		StringBuffer result = new StringBuffer();
		int outputCharCount = 0;
		for (int i = 0; i < input.length; i += 3) {
			int remaining = Math.min(3, input.length - i);
			int oneBigNumber = (input[i] & 0xff) << 16
					| (remaining <= 1 ? 0 : input[i + 1] & 0xff) << 8
					| (remaining <= 2 ? 0 : input[i + 2] & 0xff);
			for (int j = 0; j < 4; j++)
				result.append(remaining + 1 > j ? SIXTY_FOUR_CHARS[0x3f & oneBigNumber >> 6 * (3 - j)]
						: '=');
			if ((outputCharCount += 4) % 76 == 0)
				result.append('\n');
		}
		return result.toString();
	}
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	
	public static void main(String[] args) throws Exception {
		//带加密字符串
//		String str = "{'type':'2','serverHallId':'5201010600427320002','etcNo':'12345678901234567890','obuId':'1234567890123456'}";
//		//统一使用UTF-8，并去空格换行处理   12345678为加密key,请联系管理员获取测试与生产秘钥，并妥善保存
//		System.out.println(encode(str, Charset.forName("UTF-8"), "12345678"));

		String data = "b9f3d6dd b8dfcbd9 17115201 16402302 01751318 30303030 30303030 20202020 20202020 20202020 00123490 00";
		data = data.replace(" ", "");
		byte[] bytes = parseHexStr2Byte(data);
		ByteBuffer wrap = ByteBuffer.wrap(bytes);
		byte[] bytes1 = new byte[20];
		wrap.get(bytes1);

		int anInt = wrap.getInt();
		System.out.println(anInt);
	}

}
