package com.anyi.common.qfu;

import cn.hutool.core.io.IoUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static void sign(Map<String, Object> params, PrivateKey privateKey) throws Exception {
        String[] keys = params.keySet().stream()
            .filter(k -> !k.equals("sign") && !k.isEmpty() && !params.get(k).equals("") && params.get(k) != null && !params.get(k).getClass().isArray())
            .sorted()
            .toArray(String[]::new);

        String data = Arrays.stream(keys)
            .map(k -> k + "=" + params.get(k))
            .collect(Collectors.joining("&"));

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes("UTF-8"));
        byte[] encryptData = signature.sign();

        params.put("sign", Base64.getEncoder().encodeToString(encryptData));
    }

    public static void checkSign(Map<String, Object> params, PublicKey publicKey) throws Exception {
        String[] keys = params.keySet().stream()
            .filter(k -> !k.equals("sign") && !k.isEmpty() && !params.get(k).equals("") && params.get(k) != null && !params.get(k).getClass().isArray())
            .sorted()
            .toArray(String[]::new);

        String data = Arrays.stream(keys)
            .map(k -> k + "=" + params.get(k))
            .collect(Collectors.joining("&"));

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes("UTF-8"));

        boolean verified = signature.verify(Base64.getDecoder().decode((String) params.get("sign")));
        if (!verified) {
            throw new Exception("应答验签失败");
        }
    }

    public static PublicKey readPublicKey(String resourcePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream inputStream = new FileInputStream(resourcePath);
        byte[] keyBytes = IoUtil.readBytes(inputStream);
        inputStream.close();
        String rawString = (new String(keyBytes)).replaceAll("\\s*-----BEGIN PUBLIC KEY-----\\s*", "")
            .replaceAll("\\s*-----END PUBLIC KEY-----\\s*", "").replaceAll("\\s", "");
        byte[] decodedBytes = Base64.getDecoder().decode(rawString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey readPrivateKey(String resourcePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream inputStream = new FileInputStream(resourcePath);
        byte[] keyBytes = IoUtil.readBytes(inputStream);
        inputStream.close();
        String rawString = (new String(keyBytes)).replaceAll("\\s*-----BEGIN PRIVATE KEY-----\\s*", "")
            .replaceAll("\\s*-----END PRIVATE KEY-----\\s*", "").replaceAll("\\s", "");
        byte[] decodedBytes = Base64.getDecoder().decode(rawString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PublicKey readPublicKey(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = IoUtil.readBytes(inputStream);
        inputStream.close();
        String rawString = (new String(keyBytes)).replaceAll("\\s*-----BEGIN PUBLIC KEY-----\\s*", "")
                .replaceAll("\\s*-----END PUBLIC KEY-----\\s*", "").replaceAll("\\s", "");
        byte[] decodedBytes = Base64.getDecoder().decode(rawString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey readPrivateKey(InputStream inputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = IoUtil.readBytes(inputStream);
        inputStream.close();
        String rawString = (new String(keyBytes)).replaceAll("\\s*-----BEGIN PRIVATE KEY-----\\s*", "")
                .replaceAll("\\s*-----END PRIVATE KEY-----\\s*", "").replaceAll("\\s", "");
        byte[] decodedBytes = Base64.getDecoder().decode(rawString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}