package com.anyi.sparrow.organize.employee.service;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.TokenChecker;
import com.anyi.sparrow.organize.employee.dao.mapper.TemporaryEmployeeLoginMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.TemporaryEmployeeMapper;
import com.anyi.sparrow.organize.employee.domain.TemporaryEmployee;
import com.anyi.common.employee.enums.EmStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.anyi.sparrow.organize.employee.domain.TemporaryEmployeeLogin;
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
public class TemporaryEmployeeTokenChecker implements TokenChecker {
    @Autowired
    private TemporaryEmployeeMapper dao;
    @Autowired
    private TemporaryEmployeeLoginMapper loginDao;

    @Override
    public LoginUser check(String token) {
        TemporaryEmployeeLogin login = getByToken(token);
        Date now = new Date();
        if(now.compareTo(login.getTokenExpire()) > 0) {
            throw new BusinessException(SystemError.TOKEN_EXPIRE);
        }
        TemporaryEmployee employee = getEmployee(login.getUserId());
        if(employee.getStatus() == EmStatus.CANCEL.getCode()) {
            throw new BusinessException(SystemError.TOKEN_INVALID);
        }
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

        Date date = login.getTokenExpire();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        loginUser.setTokenExpire(localDateTime);
        return loginUser;
    }

    private TemporaryEmployeeLogin getByToken(String token) {
        if (StrUtil.isBlank(token)) {
            throw new BaseException(SystemError.TOKEN_INVALID);
        }
        TemporaryEmployeeLogin login = loginDao.selectOne(new LambdaQueryWrapper<TemporaryEmployeeLogin>().eq(TemporaryEmployeeLogin::getToken, token));
        if (login == null) {
            throw new BaseException(SystemError.TOKEN_INVALID);
        }
        return login;
    }

    private TemporaryEmployee getEmployee(Long userId) {
        TemporaryEmployee emp = dao.selectById(userId);
        if (emp == null) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        return emp;
    }
}