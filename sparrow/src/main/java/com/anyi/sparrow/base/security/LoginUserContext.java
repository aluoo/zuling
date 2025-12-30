package com.anyi.sparrow.base.security;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.anyi.common.util.LoggerUtil;

public class LoginUserContext {
    private static final ThreadLocal<LoginUser> holder = new ThreadLocal<>();

    public static void set(LoginUser loginUser) {
        holder.set(loginUser);
    }

    public static void remove() {
        holder.remove();
    }

    public static LoginUser getUser() {
        return holder.get();
    }

    public static LoginUser getUserByCatch() {
        try {
            return getUser();
        } catch (Exception e) {
            LoggerUtil.debug("LoginUserContext.getUserByCatch.error: get user failed - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }

    public static Long getUserIdByCatch() {
        try {
            return getUser().getId();
        } catch (Exception e) {
            LoggerUtil.debug("LoginUserContext.getUserIdByCatch.error: get user failed - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }
}