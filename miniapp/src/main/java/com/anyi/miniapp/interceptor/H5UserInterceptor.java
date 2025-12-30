/*
package com.anyi.miniapp.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.anyi.common.constant.CacheDefine;
import com.anyi.common.domain.entity.Etc;
import com.anyi.common.enums.EtcStatusEnum;
import com.anyi.common.enums.ResultCodeEnum;
import com.anyi.common.result.ResponseDTO;
import com.anyi.miniapp.entity.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

*/
/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/7
 *//*

@Slf4j
public class H5UserInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;
    private final IUserAccountService userAccountService;
    private final IEtcService etcService;

    public H5UserInterceptor(RedisTemplate<String, String> redisTemplate, IUserAccountService userAccountService, IEtcService etcService) {
        this.redisTemplate = redisTemplate;
        this.userAccountService = userAccountService;
        this.etcService = etcService;
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            String mobile = request.getHeader(UserManager.X_ACCESS_MOBILE);
            log.info("mobile: {}", mobile);
            log.info("platform: {}", request.getHeader("X-Client-Platform"));
            log.info("version: {}", request.getHeader("X-Client-Version"));
            if (StrUtil.isBlank(mobile)) {
                returnEmpty(response);
                return false;
            }
            UserAccount ua = getByMobile(mobile);
            if (ua == null) {
                returnEmpty(response);
                return false;
            }
            UserContextHolder.getInstance().setContext(ua);
            return true;
        } catch (Exception e) {
            log.error("", e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder instance = UserContextHolder.getInstance();
        if (null != instance) {
            instance.setContext(null);
        }
    }

    private UserAccount getByMobile(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return null;
        }
        String userKey = CacheDefine.H5UserToken.getUserKey(mobile);
        String userValue = redisTemplate.opsForValue().get(userKey);
        UserAccount ua = null;
        if (StrUtil.isNotBlank(userValue)) {
            ua = JSON.parseObject(userValue, UserAccount.class);
        } else {
            Etc etc = etcService.lambdaQuery()
                    .select(Etc::getUserId)
                    .eq(Etc::getMobileNumber, mobile)
                    .in(Etc::getEtcStatus, Arrays.asList(EtcStatusEnum.Normal.getCode(), EtcStatusEnum.Violate_list.getCode()))
                    .last("limit 1")
                    .one();
            if (etc != null && etc.getUserId() != null) {
                ua = userAccountService.getById(etc.getUserId());
            }
            //ua = userAccountService.lambdaQuery().eq(UserAccount::getMobileNumber, mobile).one();
            if (ua != null) {
                redisTemplate.opsForValue().set(userKey, JSON.toJSONString(ua), 1, TimeUnit.DAYS);
            }
        }
        return ua;
    }

    private void returnEmpty(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置返回200 和响应编码
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("Application/json;charset=utf-8");
        // 构造返回响应体

        String resultString = JSONUtil.toJsonStr(ResponseDTO.response(ResultCodeEnum.SUCCESS));
        //log.error(resultString);
        outputStream.write(resultString.getBytes(StandardCharsets.UTF_8));
    }
}*/
