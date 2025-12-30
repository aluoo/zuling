package com.anyi.miniapp.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.anyi.common.constant.CacheDefine;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.enums.ResultCodeEnum;
import com.anyi.common.result.ResponseDTO;
import com.anyi.common.user.domain.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@Slf4j
public class MobileInterceptor implements HandlerInterceptor {
    private static final String LOGIN_FAIL_DISPATCHER = "/error/loginFail";
    private final RedisTemplate<String, String> redisTemplate;
    private final EmployeeService employeeService;

    public MobileInterceptor(RedisTemplate<String, String> redisTemplate, EmployeeService employeeService) {
        this.redisTemplate = redisTemplate;
        this.employeeService = employeeService;
    }


    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        try {
            String token = request.getHeader(UserManager.X_ACCESS_TOKEN);
            log.info("token:{}", token);
            if (StringUtils.isEmpty(token)) {
                returnNoLogin(response);
                //request.getRequestDispatcher(LOGIN_FAIL_DISPATCHER).forward(request, response);
                return false;
            }

            //todo 从redis读取user信息
            String userKey = CacheDefine.UserToken.getUserKey(token);
            String userValue = redisTemplate.opsForValue().get(userKey);

            if (StringUtils.isEmpty(userValue)) {
                returnNoLogin(response);
                //request.getRequestDispatcher(LOGIN_FAIL_DISPATCHER).forward(request, response);
                return false;
            }

            UserAccount userAccount = JSON.parseObject(userValue, UserAccount.class);
            if (userAccount == null || StrUtil.isBlank(userAccount.getMobileNumber())) {
                returnNoLogin(response);
                //request.getRequestDispatcher(LOGIN_FAIL_DISPATCHER).forward(request, response);
                return false;
            }

            Employee employee = employeeService.getByMobileStatus(userAccount.getMobileNumber());
            if (employee == null) {
                returnNoEmployee(response);
                return false;
            }
            userAccount.setEmployeeId(employee.getId());
            userAccount.setCompanyId(employee.getCompanyId());
            log.info("user:{}", JSON.toJSONString(userAccount));
            UserContextHolder.getInstance().setContext(userAccount);
            return true;// 放行
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

    /**
     * 返回未登录的错误信息
     *
     * @param response ServletResponse
     */
    private void returnNoLogin(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置返回401 和响应编码
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("Application/json;charset=utf-8");
        // 构造返回响应体

        String resultString = JSONUtil.toJsonStr(ResponseDTO.response(ResultCodeEnum.TOKEN_INVALID));
        log.error(resultString);
        outputStream.write(resultString.getBytes(StandardCharsets.UTF_8));
    }

    private void returnNoEmployee(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置返回401 和响应编码
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("Application/json;charset=utf-8");
        // 构造返回响应体

        String resultString = JSONUtil.toJsonStr(ResponseDTO.response(ResultCodeEnum.EMPLOYEE_NOT_EXIST));
        log.error(resultString);
        outputStream.write(resultString.getBytes(StandardCharsets.UTF_8));
    }
}