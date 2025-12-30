package com.anyi.sparrow.applet.user.service;

import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.user.service.UserAccountService;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.TokenChecker;
import com.anyi.common.employee.domain.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class UserTokenChecker implements TokenChecker {
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserAccountService accountService;

    @Override
    public LoginUser check(String token) {
        UserLogin login = userLoginService.getByToken(token);
        if(login == null) {
            throw new BusinessException(SystemError.TOKEN_INVALID);
        }
        LocalDateTime now = LocalDateTime.now();
        if(now.compareTo(login.getTokenExpire()) > 0) {
            throw new BusinessException(SystemError.TOKEN_EXPIRE);
        }
        UserAccount account = accountService.getById(login.getUserId());
        if(account == null) {
            throw new BusinessException(SystemError.TOKEN_INVALID);
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setName(account.getNickname());
        loginUser.setNickname(account.getNickname());
        loginUser.setMobileNumber(account.getMobileNumber());
        loginUser.setId(account.getId());
        loginUser.setOpenId(account.getOpenId());
        loginUser.setTokenExpire(login.getTokenExpire());
        return loginUser;
    }
}
