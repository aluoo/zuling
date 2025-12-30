package com.anyi.sparrow.cyx.wrapper;

import cn.hutool.core.lang.generator.UUIDGenerator;
import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSONObject;
import com.anyi.sparrow.cyx.util.AccessAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * @author shenbinhong
 * @date 2022年02月08日 16:52
 */
@Slf4j
public class CyxApiRequestWrapper extends HttpServletRequestWrapper {

    private final String body;
    private final String contentType;
    private final String requestId;
    private final String requestIp;
    private JSONObject jsonBody = new JSONObject();

    public String getRequestId() {
        return requestId;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public CyxApiRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.contentType = request.getContentType();
        this.body = getBodyString(request);
        this.requestId =   new UUIDGenerator().next();
        this.requestIp =  AccessAddressUtil.getIpAddress(request);
        initParamObject();

        log.info("=========jsonBody {} =============", this.jsonBody);
    }

    public String getBody() {
        return this.body;
    }

    public JSONObject getJsonBody() {
        return this.jsonBody;
    }

    private void initParamObject() {
        if (StringUtils.isNotEmpty(this.body) && StringUtils.isNotEmpty(this.contentType)) {
            if (this.contentType.contains(ContentType.MULTIPART.getValue()) || this.contentType.contains(ContentType.FORM_URLENCODED.getValue())) {
                if (this.body.contains("&")) {
                    String[] splits = this.body.split("&");
                    if (splits.length > 0) {
                        for (int i = 0; i < splits.length; i++) {
                            if (StringUtils.isNotBlank(splits[i])) {
                                if (splits[i].contains("=")) {
                                    String[] keyValuePair = splits[i].split("=");
                                    if (keyValuePair.length == 1) {
                                        this.jsonBody.put(keyValuePair[0], "");
                                    } else if (keyValuePair.length == 2) {
                                        if (keyValuePair[1].contains(",")) {
                                            String values = keyValuePair[1].replace("[", "").replace("]", "");
                                            this.jsonBody.put(keyValuePair[0], Arrays.asList(values.split(",")));
                                        } else {
                                            this.jsonBody.put(keyValuePair[0], keyValuePair[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.contentType.contains(ContentType.JSON.getValue())) {
                this.jsonBody = JSONObject.parseObject(this.body);
            }


        }

    }

    public static JSONObject initParamObject(String body, String contentType) {

        JSONObject jsonBody = new JSONObject();
        if (StringUtils.isNotEmpty(body) && StringUtils.isNotEmpty(contentType)) {
            if (contentType.contains(ContentType.MULTIPART.getValue()) || contentType.contains(ContentType.FORM_URLENCODED.getValue())) {
                if (body.contains("&")) {
                    String[] splits = body.split("&");
                    if (splits.length > 0) {
                        for (int i = 0; i < splits.length; i++) {
                            if (StringUtils.isNotBlank(splits[i])) {
                                if (splits[i].contains("=")) {
                                    String[] keyValuePair = splits[i].split("=");
                                    if (keyValuePair.length == 1) {
                                        jsonBody.put(keyValuePair[0], "");
                                    } else if (keyValuePair.length == 2) {
                                        if (keyValuePair[1].contains(",")) {
                                            String values = keyValuePair[1].replace("[", "").replace("]", "");
                                            jsonBody.put(keyValuePair[0], Arrays.asList(values.split(",")));
                                        } else {
                                            jsonBody.put(keyValuePair[0], keyValuePair[1]);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }

            if (contentType.contains(ContentType.JSON.getValue())) {
                jsonBody = JSONObject.parseObject(body);
            }
        }

        return jsonBody;
    }

    public String getBodyString(final HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        String bodyString = "";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(contentType) && (contentType.contains("multipart/form-data") || contentType.contains("x-www-form-urlencoded"))) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> next : parameterMap.entrySet()) {
                String[] values = next.getValue();
                String value = null;
                if (values != null) {
                    if (values.length == 1) {
                        value = values[0];
                    } else {
                        value = Arrays.toString(values);
                    }
                }
                sb.append(next.getKey()).append("=").append(value).append("&");
            }

            if (sb.length() > 0) {
                bodyString = sb.toString().substring(0, sb.toString().length() - 1);
            }
            return bodyString;
        }
        return IOUtils.toString(request.getInputStream());
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
