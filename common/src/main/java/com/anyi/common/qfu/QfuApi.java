package com.anyi.common.qfu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QfuApi {
    private static final String BASE_URL = "https://xopen.luodi.vip";

    final private Credentials credentials;

    public QfuApi(Credentials credentials) {
        this.credentials = credentials;
    }

    public Map<String, Object> walletList() throws Exception {
        return this.request("/wallet/list");
    }

    public Map<String, Object> walletBalance(String merchant) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("merchant", merchant);
        return this.request("/wallet/balance", data);
    }

    public Map<String, Object> walletMetadata(String merchant) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("merchant", merchant);
        return this.request("/wallet/metadata", data);
    }

    public Map<String, Object> walletTransfer(String merchant, long start, long finish, int offset, int limit) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("merchant", merchant);
        data.put("start", start);
        data.put("finish", finish);
        data.put("offset", offset);
        data.put("limit", limit);
        return this.request("/wallet/transfer", data);
    }

    public Map<String, Object> paymentCompute(String payload) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("payload", payload);
        return this.request("/payment/compute", data);
    }

    public Map<String, Object> paymentInvoke(String merchant, String order, String account, int value, String name, String identity, String phone, String remarks, String batch, String title) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("merchant", merchant);
        data.put("order", order);
        data.put("account", account);
        data.put("value", value);
        data.put("name", name);
        data.put("identity", identity);
        data.put("phone", phone);
        if (remarks!= null && !remarks.isEmpty()) {
            data.put("remarks", remarks);
        }
        if (batch != null && !batch.isEmpty()) {
            data.put("batch", batch);
        }
        if (title != null && !title.isEmpty()) {
            data.put("title", title);
        }
        return this.request("/payment/invoke", data);
    }

    @Deprecated
    public Map<String, Object> paymentCheck(String vendor, String agency, String order) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("vendor", vendor);
        data.put("agency", agency);
        data.put("order", order);

        return this.request("/payment/check", data);
    }

    public Map<String, Object> paymentCheck(String order) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        return this.request("/payment/check", data);
    }

    private Map<String, Object> request(String url) throws Exception {
        return request(url, new HashMap<>());
    }

    private Map<String, Object> request(String url, Map<String, Object> data) throws Exception {
        data.put("vendor", this.credentials.getVendor());
        if (this.credentials.getAgency() != null) {
            data.put("agency", this.credentials.getAgency());
        }
        data.put("nonce", this.nonce());
        data.put("timestamp", System.currentTimeMillis());
        Utils.sign(data, this.credentials.getPrivateKey());

        URL urlObj = new URL(BASE_URL + url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setDoOutput(true);
        String jsonPayload = new Gson().toJson(data);

        conn.getOutputStream().write(jsonPayload.getBytes());

        log.info("qfu-api-request: url-{}, data-{}", urlObj, data);
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        conn.disconnect();

        int code = conn.getResponseCode();

        if (code >= 400 && code < 600) {
            log.info("qfu-api-resp-error: code: {}", code);
            throw new Exception("返回错误，错误码：" + code);
        }

        String responseBody = response.toString();

        Gson gson = new GsonBuilder()
                .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create();
        HashMap<String, Object> res = gson.fromJson(responseBody, HashMap.class);
        Utils.checkSign(res, this.credentials.getPublicKey());

        log.info("qfu-api-response: {}", res);
        return res;
    }

    private String nonce() {
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 20;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charset.length());
            char randomChar = charset.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}