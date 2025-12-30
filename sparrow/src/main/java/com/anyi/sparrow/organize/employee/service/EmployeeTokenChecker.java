package com.anyi.sparrow.organize.employee.service;

import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.TokenChecker;
import com.anyi.sparrow.organize.employee.dao.EmployeeDao;
import com.anyi.sparrow.organize.employee.dao.EmployeeLoginDao;
import com.anyi.common.employee.domain.Employee;
import com.anyi.sparrow.organize.employee.domain.EmployeeLogin;
import com.anyi.common.employee.enums.EmStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EmployeeTokenChecker implements TokenChecker {
    @Autowired
    private EmployeeLoginDao dao;

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public LoginUser check(String token) {
        EmployeeLogin login = dao.getByToken(token);
        if(login == null) {
            throw new BaseException(SystemError.TOKEN_INVALID);
        }
        Date now = new Date();
        if(now.compareTo(login.getTokenExpire()) > 0) {
            throw new BusinessException(SystemError.TOKEN_EXPIRE);
        }
        Employee employee = employeeDao.getById(login.getUserId());
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
        loginUser.setLevel(employee.getLevel());
        loginUser.setAncestors(employee.getAncestors());

        Date date = login.getTokenExpire();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        loginUser.setTokenExpire(localDateTime);
        return loginUser;
    }
}
