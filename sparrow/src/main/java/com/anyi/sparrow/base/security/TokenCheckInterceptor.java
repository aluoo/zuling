package com.anyi.sparrow.base.security;

import com.alibaba.fastjson.JSONObject;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TokenCheckInterceptor extends HandlerInterceptorAdapter {

    private final static Map<String, String> TOKEN_MAP_PATH = new HashMap<>();
    private final static String TOKEN_BLACK_COMMON = "black_lL2)wA3+wT5{dF4`bX3^wR3+hX7=hT";


    private Map<Integer, TokenChecker> tokenCheckers = new HashMap<>();

    private RedisTemplate<String, String> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TokenCheckInterceptor.class);

    public TokenCheckInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;

        TOKEN_MAP_PATH.put("/etc-black/v1/addToBlack", TOKEN_BLACK_COMMON);
        TOKEN_MAP_PATH.put("/etc-black/v1/addToWhite", TOKEN_BLACK_COMMON);
        TOKEN_MAP_PATH.put("/etc-bill/v1/sendBillResults", TOKEN_BLACK_COMMON);
        TOKEN_MAP_PATH.put("/etc-bill/v1/deduction/order/result", TOKEN_BLACK_COMMON);
    }

    /**
     * 添加token checker
     *
     * @param userType
     * @param checker
     */
    public void addTokenChecker(int userType, TokenChecker checker) {
        tokenCheckers.put(userType, checker);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //preflight 浏览器自带发起的一中跨域请求，这时候不会携带TOKEN只能默认通过校验
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        String token = request.getHeader("x-access-token");
        if (StringUtils.isEmpty(token)) {
            logger.error("未携带token的请求 path:{}", request.getRequestURI());
            throw new BusinessException(SystemError.TOKEN_INVALID);
        }

        String requestPath = request.getRequestURI();
        String configToken = TOKEN_MAP_PATH.get(requestPath);

        if (!StringUtils.isEmpty(configToken) && token.equals(configToken)) {
            logger.info("token:{},path:{}", requestPath, token);
            return true;
        }

        try {
            int userType = TokenBuilder.parseUserType(token);
            String value = redisTemplate.opsForValue().get(Constants.TOKEN_PREFIX + userType + ":" + token);
            LoginUser loginUser = null;
            if (StringUtils.isEmpty(value)) {
                TokenChecker checker = tokenCheckers.get(userType);
                loginUser = checker.check(token);
                if (Objects.nonNull(loginUser)) {
                    long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), loginUser.getTokenExpire());
                    redisTemplate.opsForValue().set(Constants.TOKEN_PREFIX + userType + ":" + token, JSONObject.toJSONString(loginUser),
                            seconds, TimeUnit.SECONDS);
                }
            } else {
                loginUser = JSONObject.parseObject(value, LoginUser.class);
            }
            loginUser.setLoginUserType(userType);

            LoginUserContext.set(loginUser);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("请求的TOKEN是{}", token);
            throw new BusinessException(SystemError.TOKEN_INVALID, e.getLocalizedMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserContext.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    private String makeEmInfo(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            String mobile = request.getHeader("mobile");
            String companyName = request.getHeader("companyName");
            String companyId = request.getHeader("companyId");
            String deptName = request.getHeader("deptName");
            String deptId = request.getHeader("deptId");
            String userName = request.getHeader("userName");
            String userId = request.getHeader("userId");
            String platform = request.getHeader("platform");
            try {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(companyName)) {
                    companyName = URLDecoder.decode(companyName, "UTF-8");
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(deptName)) {
                    deptName = URLDecoder.decode(deptName, "UTF-8");
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(userName)) {
                    userName = URLDecoder.decode(userName, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("makeEmInfo error", e);
            }
            String join = org.apache.commons.lang3.StringUtils.join(new String[]{mobile, companyId, companyName, deptId, deptName, userId, userName, platform}, "||");
            sb.append(join);
        } catch (Exception e) {
            logger.error("makeEmInfo error", e);
        }
        return sb.toString();
    }
}