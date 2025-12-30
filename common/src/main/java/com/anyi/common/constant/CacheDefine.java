package com.anyi.common.constant;


import com.anyi.common.enums.UserTypeEnum;

/**
 * 缓存key结构定义
 */
public interface CacheDefine {

    String PREFIX = "anyi";
    String SP = ":";
    String DOT = ".";
    String U = "_";
    
    interface UserToken {
        static String getUserKey(String token) {
            return "token" + SP + UserTypeEnum.MINIAPP_USER.getCode() + SP + token;
        }
    }

    interface H5UserToken {
        static String getUserKey(String mobile) {
            return "token" + SP + UserTypeEnum.H5_TEMP_USER.getCode() + SP + mobile;
        }
    }

    interface ExchangePhoneToolToken {
        static String getUserKey(String mobile) {
            return "token" + SP + UserTypeEnum.EXCHANGE_PHONE_TOOL_USER.getCode() + SP + mobile;
        }
    }
}