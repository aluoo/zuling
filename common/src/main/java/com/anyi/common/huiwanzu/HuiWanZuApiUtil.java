package com.anyi.common.huiwanzu;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.huiwanzu.dto.HuiWanZuCommonResp;
import com.anyi.common.huiwanzu.enums.HuiWanZuApiEnum;
import com.anyi.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/6/9
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class HuiWanZuApiUtil {
    private static final String APP_ID_TEST = "s2chmpaj52s9d774";
    private static final String APP_SECRET_TEST = "nd777ulkgqbx8507jtnw5yctswto1m3n";
    private static final String APP_ID = "s78zh6yco92x07h3";
    private static final String APP_SECRET = "009i12mo1g67b6lu2tuay5fujqtlznrp";
    private static final String TEST_API_HOST = "http://sandbox-zjs-open-api.yyctrade.com";
    private static final String PROD_API_HOST = "https://zjs-open-api.yyctrade.com";

    public static void main(String[] args) {
        Map<String, Object> body = new HashMap<>();
        body.put("saleOrderId", 233419142176837L); // 订单号
        // body.put("saleOrderId", "233419142176837"); // 字符串订单号也行
        // body.put("saleOrderId", "123"); // 没有订单，会报系统错误
        HuiWanZuCommonResp resp = execBase(body, TEST_API_HOST, HuiWanZuApiEnum.ORDERS_GET_BY_ID);
        System.out.println(JSONUtil.toJsonStr(resp));
    }

    public static String getHost(String env) {
        return "production".equals(env) ? PROD_API_HOST : TEST_API_HOST;
    }

    public static String getAppId(String env) {
        return "production".equals(env) ? APP_ID : APP_ID_TEST;
    }

    public static String getAppSecret(String env) {
        return "production".equals(env) ? APP_SECRET : APP_SECRET_TEST;
    }

    public static HuiWanZuCommonResp execBase(Object req, String host, HuiWanZuApiEnum api) {
        String url = host + api.getPath();
        return execBase(req, url, api.getMethod(), api.getContentType());
    }

    private static HuiWanZuCommonResp execBase(Object req, String url, Method method, ContentType contentType) {
        log.info("hwz-api-info: url-{}, req-{}", url, JSONUtil.toJsonStr(req));
        HttpRequest request = HttpUtil.createRequest(method, url);
        request.contentType(contentType.getValue());
        if (Objects.equals(contentType, ContentType.JSON)) {
            request.body(JSONUtil.toJsonStr(req));
        } else {
            request.form(JSONUtil.parseObj(req));
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        request.header("OPEN-APPID", getAppId(SpringContextUtil.getProfile()))
                .header("OPEN-TS", timestamp)
                .header("OPEN-SIGN", sign(JSONUtil.toJsonStr(req), timestamp));
        try (HttpResponse response = request.execute()) {
            log.info("hwz-api-info: url-{}, res-{}", url, JSONUtil.toJsonStr(response.body()));
            if (!response.isOk() || Objects.isNull(response.body())) {
                log.error("hwz-api: error response {}", JSONUtil.toJsonStr(response));
                throw new BusinessException(-1, "hwz api error");
            }
            HuiWanZuCommonResp commonResp = JSONUtil.toBean(response.body(), HuiWanZuCommonResp.class);
            if (commonResp.getCode() != 200) {
                log.error("hwz-api: error response {}-{}", commonResp.getCode(), commonResp.getMsg());
                throw new BusinessException(-1, StrUtil.format("hwz {}-{}", commonResp.getCode(), commonResp.getMsg()));
            }
            return commonResp;
        }
    }

    private static String sign(String bodyStr, String timestamp) {
        String beforeHash = bodyStr + timestamp + getAppSecret(SpringContextUtil.getProfile());
        return SecureUtil.md5(beforeHash);
    }
}