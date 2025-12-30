package com.anyi.sparrow.base.security;

public interface TokenChecker {
    /**
     * 检查token
     * @param token
     * @return
     */
    LoginUser check(String token);
}
