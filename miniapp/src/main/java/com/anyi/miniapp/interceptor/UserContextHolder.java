package com.anyi.miniapp.interceptor;

import com.anyi.common.user.domain.UserAccount;

/**
 * 用户上下文
 */
public class UserContextHolder {

    private final ThreadLocal<UserAccount> threadLocal;

    private UserContextHolder() {
        this.threadLocal = new ThreadLocal<>();
    }

    /**
     * 创建实例
     *
     * @return
     */
    public static UserContextHolder getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * 静态内部类单例模式
     * 单例初使化
     */
    private static class SingletonHolder {
        private static final UserContextHolder sInstance = new UserContextHolder();
    }

    /**
     * 用户上下文中放入信息
     *
     */
    public void setContext(UserAccount userAccount) {
        threadLocal.set(userAccount);
    }

    /**
     * 获取上下文中的信息
     *
     * @return
     */
    public UserAccount getContext() {
        return threadLocal.get();
    }



    /**
     * 清空上下文
     */
    public void clear() {
        threadLocal.remove();
    }

}
