package com.anyi.sparrow.applet.user.service;

import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.user.service.UserAccountService;
import com.anyi.sparrow.applet.mapstruct.UserConverter;
import com.anyi.sparrow.applet.user.vo.PhoneNumberVO;
import com.anyi.sparrow.applet.user.vo.UpdateUserDTO;
import com.anyi.sparrow.applet.user.vo.UserInfoVO;
import com.anyi.sparrow.applet.user.vo.UserTokenVO;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.vo.WxOpenId;
import com.anyi.sparrow.wechat.service.WechatMiniHandler;
import com.anyi.sparrow.wechat.vo.WechatLoginDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class UserAccountProcessService {

    @Autowired
    private WechatMiniHandler wechatMiniHandler;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private EmployeeService employeeService;

    private UserAccount getAndSaveUserAccountByUnionId(WxOpenId wxOpenId) {
        UserAccount userAccount = userAccountService.selectByUnionID(wxOpenId.getUnionId());
        if (Objects.isNull(userAccount)) {
            userAccount = new UserAccount();
            userAccount.setUnionId(wxOpenId.getUnionId());
        }
        if (StringUtils.isBlank(userAccount.getOpenId())) {
            //小程序用户第一次授权登陆
            userAccount.setOpenId(wxOpenId.getOpenId());
            userAccount.setCreateTime(new Date());
            userAccount.setUpdateTime(new Date());
            userAccountService.saveOrUpdate(userAccount);
        }
        return userAccount;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserAccount loginByCode(String authCode) {
        WxOpenId wxOpenId = wechatMiniHandler.getOpenId(authCode);
        return this.getAndSaveUserAccountByUnionId(wxOpenId);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserTokenVO loginByCode(WechatLoginDTO loginDTO) {
        WxOpenId wxOpenId = wechatMiniHandler.getOpenId(loginDTO.getAuthCode());
        UserAccount userAccount = this.getAndSaveUserAccountByUnionId(wxOpenId);
        return userLoginService.userLogin(userAccount);
    }

    @Transactional(rollbackFor = Exception.class)
    public PhoneNumberVO bindPhoneNumber(String authCode) {
        String phoneNumber = wechatMiniHandler.getPhoneNumber(authCode);

        LoginUser user = LoginUserContext.getUser();

        UserAccount userAccount = userAccountService.getById(user.getId());
        userAccount.setId(user.getId());
        userAccount.setMobileNumber(phoneNumber);
        userAccount.setUpdateTime(new Date());
        userAccountService.updateById(userAccount);

        userLoginService.refreshUser(userAccount);
        return new PhoneNumberVO(phoneNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserTokenVO mobileLogin(String authCode) {
        String phoneNumber = wechatMiniHandler.getPhoneNumber(authCode);
        Employee employee = employeeService.getRecyclerByMobileStatus(phoneNumber);
        if (ObjectUtil.isNull(employee)) {
            throw new BusinessException(BizError.USER_LOGIN_ERROR);
        }
        UserAccount userAccount = userAccountService.selectByMobile(phoneNumber);
        if (ObjectUtil.isNull(userAccount)) {
            throw new BusinessException(BizError.USER_LOGIN_ERROR);
        }
        return userLoginService.userLogin(userAccount);
    }

    public UserInfoVO getUserInfo() {
        LoginUser user = LoginUserContext.getUser();

        return UserConverter.INSTANCE.logUser2InfoVO(user);
    }

    public UserInfoVO updateUser(UpdateUserDTO userDTO) {
        LoginUser user = LoginUserContext.getUser();
        UserAccount userAccount = userAccountService.getById(user.getId());
        userAccount.setId(user.getId());
        userAccount.setNickname(userDTO.getNickname());
        userAccount.setAvatarUrl(userDTO.getAvatarUrl());
        userAccount.setUpdateTime(new Date());
        userAccountService.updateById(userAccount);
        userLoginService.refreshUser(userAccount);
        return UserConverter.INSTANCE.user2InfoVO(userAccount);
    }

}