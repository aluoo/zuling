package com.anyi.sparrow.common.utils;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Sha256WithRsa {
    private static final String KEY_ALGORITHM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private static final Logger logger = LoggerFactory.getLogger(Sha256WithRsa.class);

    /**
     * /**
     * 还原公钥
     *
     * @param keyBytes
     * @return
     */
    public static PublicKey restorePublicKey(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原私钥
     *
     * @param keyBytes
     * @return
     */
    public static PrivateKey restorePrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory
                    .generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成密钥对
     *
     * @return
     */
    public static void generateKeyBytes() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println(Base64.toBase64String(publicKey.getEncoded()));
            System.out.println(Base64.toBase64String(privateKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * sha256WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String rsa256Sign(String content, String privateKey) {
        PrivateKey priKey = restorePrivateKey(Base64.decode(privateKey));
        try {
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGNATURE_ALGORITHM);

            signature.initSign(priKey);

            signature.update(content.getBytes(StandardCharsets.UTF_8));

            byte[] signed = signature.sign();
            return Base64.toBase64String(signed);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            throw new BusinessException(BizError.NET_ERROR);
        }
    }

    public static boolean rsa256Check(String content, String sign, String publicKey) throws Exception {
        PublicKey pubKey = restorePublicKey(Base64.decode(publicKey));

        java.security.Signature signature = java.security.Signature
                .getInstance(SIGNATURE_ALGORITHM);

        signature.initVerify(pubKey);

        signature.update(content.getBytes(StandardCharsets.UTF_8));

        return signature.verify(Base64.decode(sign));
    }

    public static void main(String[] args) throws Exception{
        String str = "1111111111111111";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGDPFbYsZTAsIzSmjNsaUmYKSPUfjHziw/IgiMhRsnOzrNoB5Nxmkr/Qewp94jNFH/0VAV8zZ630AiNjlIdNoYc9sWRwH6kbR0v6q/7baay7p0KoYXZvuU5mfCxLR6mRrzB22UuijpuyW8M8Bx3dmg/u/rMaoeXJNoW3jI83rPTkRAMZ4EOlAQ/8BovgouVvze+cfMI901QM/Ws046YZMCULpbeKSTWxZ5vxv6v8E2yA2emtbhbsKN0HzzVvmMfSdXPgi7EPG69sJBYIS6lX4oC/3KTKL6q9rRhv57rIzoFgG+v/Z5SyPU6oYQjE8QrERKgWcgr/XyN+EDrrjTUB51AgMBAAECggEASlazInLMeQx2lesV47w4UegWWkSjKmrzrq84/nRycncuTQoWxBrcCEDyGs19KxjAwJVbTFDsTYexEUfwzHXA3P6i6N/IAST8ZiJElttFAOMqK3HJmwDJU/HSaPic/cAaadwcsPXr6nKWoqAwgPT+04GNm2iXZu7/8EeJ3CbqXDvDgo/NEPHrQ4w4AKcrJy+A+Y89KEOsWXUJIYMZwEw5FxY60wJQhc+ro/fxxsRAALtANKEIGLtA0+VH6Fc1euiJHnm2xXkWyEAnXo53cXvS1xZ2KF7QYw+iSFIRoOU9AXRTcamIJjh4CJvxlm4sCW5rH/XWdlKNVQNxs2hMhngQoQKBgQDWcOqMcZfO8+5QOgLzlyGNg9nSqwaJLLi0qpRIvbst6V2mS1GE82823bNqYFK7hGGBnSOaKUH+t8CX/i5B1dcIfdEpOcnAdYaEVk6Nxi4uXbU9h4i8+depMrM+B13hYUlo7SDUY7lGzP8kxbweYplzjNYciqpAEXCAH0GNha6uCwKBgQCgB5rO2T+/riyS+QjhogJhX6GhAGzrPeLBeQJ8MX4mo0tsIU2y3hMVSS9vkdFyh+Ox0VVzMqg3wUg3zz3O28w9yuxbbOFWVyPVV0c623lBy4NX9uHtin4qWf4dEtQ8PmEolTw5smN/0dnzyWDEmkb2u4Vg7YcCStMqLyIN9A61fwKBgQDDwRw7zM5aG49nKS3clBALEVvc1kcHHg4WzsjChoDCbpAQ6Byok1jo6wWHjBq6FySzn3EKn7MhBTkJBv3PF5hOphwz2A72Y9LCJ89Z2Jn7tYtnebw3VpOqXyAE9dCbl6Xd5OndOJO/sA8R0Hfj/Po740rpjyPPjJv4OZAr0ucc9QKBgAfjuBcgkMsDbEEms/Bjy1g/nblRSw1h9K7zJ1M2IXEINeyzy5/Y4Pn4rLUEbF4IIwqeNmdChRaSmdvehL6RCYW2Xma3fHiEPUkET4ewUE8VGHmau1K5tDSkUSnfxsTdkm6sxNZXXB5uGx+wuuuWa484eX/o1fIf/D8lKbmkYaFDAoGAc719uPYpZXrNqjiUOoQJn7qNNUCTo3T50aAxZFUvCfznfayizAA9TyUIcVq2NSSelDYGhDz25aeHE3P0zNl82zAbz4MPLDr8hh1YPUgXpp6PdZD7Yh4V34KeyaiqSh5YxMORGYBlwXOc8rgOxo6kc5Ppps04LY3TQYBestd5+cM=";

        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhgzxW2LGUwLCM0pozbGlJmCkj1H4x84sPyIIjIUbJzs6zaAeTcZpK/0HsKfeIzRR/9FQFfM2et9AIjY5SHTaGHPbFkcB+pG0dL+qv+22msu6dCqGF2b7lOZnwsS0epka8wdtlLoo6bslvDPAcd3ZoP7v6zGqHlyTaFt4yPN6z05EQDGeBDpQEP/AaL4KLlb83vnHzCPdNUDP1rNOOmGTAlC6W3ikk1sWeb8b+r/BNsgNnprW4W7CjdB881b5jH0nVz4IuxDxuvbCQWCEupV+KAv9ykyi+qva0Yb+e6yM6BYBvr/2eUsj1OqGEIxPEKxESoFnIK/18jfhA66401AedQIDAQAB";

        String s = rsa256Sign(str, privateKey);
        System.out.println(s);
        System.out.println(rsa256Check(str, s, publicKey));
    }
}

