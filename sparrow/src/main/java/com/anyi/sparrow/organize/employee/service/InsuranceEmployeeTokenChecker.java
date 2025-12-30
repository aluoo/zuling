package com.anyi.sparrow.organize.employee.service;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.insurance.domain.DiInsuranceUserAccount;
import com.anyi.common.insurance.domain.DiUserLogin;
import com.anyi.common.insurance.mapper.DiInsuranceUserAccountMapper;
import com.anyi.common.insurance.mapper.DiUserLoginMapper;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.TokenChecker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/9
 */
@Component
public class InsuranceEmployeeTokenChecker implements TokenChecker {
    @Autowired
    private DiInsuranceUserAccountMapper dao;
    @Autowired
    private DiUserLoginMapper loginDao;

    @Override
    public LoginUser check(String token) {
        DiUserLogin login = getByToken(token);
        Date now = new Date();
        if(now.compareTo(login.getTokenExpire()) > 0) {
            throw new BusinessException(SystemError.TOKEN_EXPIRE);
        }
        DiInsuranceUserAccount employee = getEmployee(login.getUserId());
        if(employee.getStatus() == EmStatus.CANCEL.getCode()) {
            throw new BusinessException(SystemError.TOKEN_INVALID);
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setId(employee.getId());
        loginUser.setMobileNumber(employee.getMobile());
        Date date = login.getTokenExpire();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        loginUser.setTokenExpire(localDateTime);
        return loginUser;
    }

    private DiUserLogin getByToken(String token) {
        if (StrUtil.isBlank(token)) {
            throw new BaseException(SystemError.TOKEN_INVALID);
        }
        DiUserLogin login = loginDao.selectOne(new LambdaQueryWrapper<DiUserLogin>().eq(DiUserLogin::getToken, token));
        if (login == null) {
            throw new BaseException(SystemError.TOKEN_INVALID);
        }
        return login;
    }

    private DiInsuranceUserAccount getEmployee(Long userId) {
        DiInsuranceUserAccount emp = dao.selectById(userId);
        if (emp == null) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        return emp;
    }
}