package com.anyi.sparrow.base.security;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import com.anyi.common.constant.CacheDefine;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.enums.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.anyi.common.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/27
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class ExchangePhoneToolInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String mobile = request.getHeader(Constants.X_ACCESS_MOBILE);
        if (StrUtil.isBlank(mobile)) {
            log.error("ExchangePhoneToolInterceptor.info: 未携带token的请求 path:{}", request.getRequestURI());
            throw new BusinessException(SystemError.TOKEN_INVALID);
        }
        try {
            LoginUser loginUser = getEmployeeByMobile(mobile);
            loginUser.setLoginUserType(UserTypeEnum.EXCHANGE_PHONE_TOOL_USER.getCode());
            LoginUserContext.set(loginUser);
        } catch (Exception e) {
            log.error("ExchangePhoneToolInterceptor.error: mobile-{}-{}", mobile, ExceptionUtil.getMessage(e));
            throw new BusinessException(SystemError.TOKEN_INVALID, e.getLocalizedMessage());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserContext.remove();
    }

    private LoginUser getEmployeeByMobile(String mobile) {
        String userKey = CacheDefine.ExchangePhoneToolToken.getUserKey(mobile);
        String userValue = redisTemplate.opsForValue().get(userKey);
        if (StrUtil.isNotBlank(userValue)) {
            return JSONUtil.toBean(userValue, LoginUser.class);
        }

        Employee employee = Optional.ofNullable(employeeService.lambdaQuery()
                .eq(Employee::getMobileNumber, mobile)
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                .one()).orElseThrow(() -> new BaseException(SystemError.TOKEN_INVALID));

        LoginUser loginUser = new LoginUser();
        loginUser.setId(employee.getId());
        loginUser.setMobileNumber(employee.getMobileNumber());
        loginUser.setName(employee.getName());
        loginUser.setStatus(employee.getStatus());
        loginUser.setType(employee.getType());
        loginUser.setDeptId(employee.getDeptId());
        loginUser.setDeptCode(employee.getDeptCode());
        loginUser.setCompanyId(employee.getCompanyId());
        loginUser.setDeptType(employee.getDeptType());
        loginUser.setCompanyType(employee.getCompanyType());
        loginUser.setLevel(employee.getLevel());
        loginUser.setAncestors(employee.getAncestors());

        redisTemplate.opsForValue().set(userKey, JSONUtil.toJsonStr(loginUser), 1, TimeUnit.DAYS);

        return loginUser;
    }
}