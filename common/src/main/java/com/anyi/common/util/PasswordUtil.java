package com.anyi.common.util;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.anyi.common.advice.BusinessException;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/6/6
 * @Copyright
 * @Version 1.0
 */
public class PasswordUtil {
    private static final String TEMPLATE = "jxz{}";

    public static String buildDefaultPassword(String mobile) {
        if (!Validator.isMobile(mobile)) {
            throw new BusinessException(-1, "手机号格式错误");
        }
        String rawPwd = StrUtil.format(TEMPLATE, mobile.substring(mobile.length() - 4));
        return SecureUtil.md5(rawPwd);
    }

    public static String md5(String password) {
        return SecureUtil.md5(password);
    }
}