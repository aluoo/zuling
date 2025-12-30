package com.anyi.sparrow.cyx.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.cyx.constant.SignInfoConstants;
import com.anyi.sparrow.cyx.context.SignInfoHolder;
import com.anyi.sparrow.cyx.wrapper.CyxApiRequestWrapper;
import com.anyi.sparrow.cyx.config.ApiSignConfig;
import com.anyi.sparrow.cyx.model.SignInfo;
import com.anyi.sparrow.cyx.util.SignatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截请求获取签名参数并保存到线程私有副本
 *
 * @author shenbh
 */
@Slf4j
//@Component
public class CheckSignInfoInterceptor implements HandlerInterceptor {

    /**
     * 时间阈值, 用于检验请求的时间是否过期 (单位：s)
     */
    private static final Long TIME_THRESHOLD = 60L;

    /**
     * 毫秒转秒需要除1000
     */
    private static final int TRANSFER = 1000;

    private ApiSignConfig apiSignConfig;

//    @Autowired
//    private IOpenAkSkService openAkSkService;

    public CheckSignInfoInterceptor() {

    }

    public CheckSignInfoInterceptor(ApiSignConfig apiSignConfig) {
        this.apiSignConfig = apiSignConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CyxApiRequestWrapper requestWrapper = (CyxApiRequestWrapper) request;

        String path = requestWrapper.getRequestURI();
        log.info("path : {}" , path);
//        // 取出sign
        String signInfoStr = request.getHeader(SignInfoConstants.SIGN_HEAD_NAME);

        if (StringUtils.isNotEmpty(signInfoStr)) {

            SignInfo signInfo = null;
            if (!JSONUtil.isJson(signInfoStr)){
                throw new BusinessException(BizError.CYX_API_SIGN_PARSE_ERROR);
            }
            signInfo = JSONUtil.toBean(signInfoStr, SignInfo.class);
            this.checkRequireParam(signInfo);

            // 判断appid 是否合法
            /*if (!SignInfoConstants.CYX_APP_ID.equals( signInfo.getAppid())){
                throw new BusinessException(BizError.CYX_API_INVALID_APP_ID);
            }*/
            if (!apiSignConfig.getAppId().equals( signInfo.getAppid())){
                throw new BusinessException(BizError.CYX_API_INVALID_APP_ID);
            }

            //获取body参数
            Map paramsMap = new HashMap();
            if (StringUtils.isNotEmpty(requestWrapper.getBody())) {
                JSONObject requestParams = requestWrapper.getJsonBody();
                if (ObjectUtil.isNotNull(requestParams)) {
                    for (Map.Entry entry : requestParams.entrySet()) {
                        paramsMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            Map<String, Object> headerSignInfo = new HashMap<>();
            headerSignInfo.put("appid", signInfo.getAppid());
//            headerSignInfo.put("nonceStr", signInfo.getNonceStr());
            headerSignInfo.put("timestamp", Long.valueOf(signInfo.getTimestamp()));
            headerSignInfo.put("signtype", signInfo.getSigntype());
            headerSignInfo.put("sign", signInfo.getSign());

            boolean resultCodeConst = SignatureUtils.sha256checkInterfaceSignature(headerSignInfo, paramsMap, apiSignConfig.getSecretKey(), path);
            if (!resultCodeConst) {
                log.info("签名验证失败：headerSignInfo=" + signInfoStr + "；paramsMap=" + JSONUtil.toJsonStr(paramsMap));
                throw new BusinessException(BizError.CYX_API_SIGN_NOT_MATCH);
            } else {
                SignInfoHolder.setSignInfo(signInfo);
            }

        } else {
            log.error("signInfo为空");
            throw new BusinessException(BizError.CYX_API_UNAUTHORIZED_ERROR);
        }

        return true;
    }

    private void checkRequireParam(SignInfo signInfo) {
        if (StringUtils.isEmpty(signInfo.getAppid())) {
            throw new BusinessException(BizError.CYX_API_MISSING_APPID);
        }
        if (StringUtils.isEmpty(signInfo.getTimestamp())) {
            throw new BusinessException(BizError.CYX_API_MISSING_TIMESTAMP);
        }
        if (StringUtils.isEmpty(signInfo.getSign())) {
            throw new BusinessException(BizError.CYX_API_MISSING_SIGN);
        }
        if (StringUtils.isEmpty(signInfo.getSigntype())) {
            throw new BusinessException(BizError.CYX_API_MISSING_SIGNTYPE);
        }
        Long currentTime = System.currentTimeMillis();
        if ((currentTime - Long.valueOf(signInfo.getTimestamp())) / TRANSFER > TIME_THRESHOLD) {
            log.error("请求超时 oldtime:{}, currentTime:{}, timeOut:{}", signInfo.getTimestamp(), currentTime, (currentTime - Long.valueOf(signInfo.getTimestamp())) / TRANSFER);
            throw new BusinessException(BizError.CYX_API_TIME_OUT);
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //DO NOTHING!
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("清除 SignInfoHolder");
        SignInfoHolder.removeSignInfo();
    }

}