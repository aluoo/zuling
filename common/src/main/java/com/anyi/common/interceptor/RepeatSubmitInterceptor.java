package com.anyi.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.anyi.common.advice.BaseException;
import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.constant.Constants;
import com.anyi.common.wrapper.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/21
 * @Copyright
 * @Version 1.0
 */
@Component
@Slf4j
public class RepeatSubmitInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                RequestWrapper requestWrapper = new RequestWrapper(request);
                if (!"POST".equals(requestWrapper.getMethod())) {
                    return true;
                }
                String token = request.getHeader(Constants.X_ACCESS_TOKEN);
                if (StrUtil.isBlank(token)) {
                    token = request.getHeader(Constants.X_ACCESS_MOBILE);
                }
                if (StrUtil.isBlank(token)) {
                    // 后续有token拦截
                    return true;
                }
                String url = requestWrapper.getRequestURI();
                String postData = readAsChars(requestWrapper);
                log.debug("RI_TEST.url: {}", url);
                log.debug("RI_TEST.postData: {}", postData);

                String signBody = StrUtil.format("{}+{}+{}", url, postData, token);
                byte[] pwdKey = "aycx@mbProDuCT".getBytes();
                HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, pwdKey);
                String sign = hMac.digestHex(signBody);
                String key = StrUtil.format("{}/{}", url, sign);
                log.debug("RI_TEST.key: {}", key);

                if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                    throw new BaseException(-1, annotation.errorMessage());
                }
                redisTemplate.opsForValue().set(key, "0", annotation.interval(), TimeUnit.MILLISECONDS);
            }
            return true;
        } else {
            return true;
        }
    }

    private String readAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}