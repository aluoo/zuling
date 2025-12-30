package com.anyi.miniapp.interceptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.anyi.common.user.domain.UserAccount;
import com.anyi.common.util.LoggerUtil;

public class UserManager {

    public static final String X_ACCESS_TOKEN = "x-access-token";
    public static final String X_ACCESS_MOBILE = "x-access-mobile";

    public static UserAccount getCurrentUser() {
        return  UserContextHolder.getInstance().getContext();
    }

    /**
     * 获取用户名称
     *
     * @return
     */
    public static String getOpenId() {
       return getCurrentUser().getOpenId();
    }

    /**
     * 获取用户手机
     * @return
     */
    public static String getMobile() {
       return getCurrentUser().getMobileNumber();
    }
    public static Long getUserId() {
       return getCurrentUser().getId();
    }
    public static String getUserName() {
       return getCurrentUser().getNickname();
    }

    public static Long getUserIdByCatch() {
        try {
            return getUserId();
        } catch (Exception e) {
            LoggerUtil.debug("UserManager.getUserIdByCatch.error: get user failed - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }

    public static UserAccount getCurrentUserByCatch() {
        try {
            return getCurrentUser();
        } catch (Exception e) {
            LoggerUtil.debug("UserManager.getCurrentUserByCatch.error: get user failed - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }
}