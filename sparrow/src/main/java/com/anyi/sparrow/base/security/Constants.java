package com.anyi.sparrow.base.security;

public class Constants {
    /**
     * 员工redis缓存token前缀
     */
    public static final String TOKEN_PREFIX = "token:";

    /**
     * 公众号缓存 前缀
     */
    public static final String MP_REDIS_PREFIX = "wechat:mp:";

    /**
     * 小程序access缓存
     */
    public static final String MINI_REDIS_PREFIX = "wechat:mini:token";

    /**
     * 公众号access缓存
     */
    public static final String GZH_REDIS_PREFIX = "wechat:mp:token";

    /**
     * 临时素材key
     */
    public static final String MEDIA_ID_KEY = "wechat:mp:mediaId";

    public static final String LOGIN_EROR_COUNT_REDIS = "pangu:loginerror:";

    /**
     * 门店推广二维码缓存KEY
     */
    public static final String SPREAD_COMPANY_KEY = "spread:company:";

    /**
     * 服务商推广二维码缓存KEY
     */
    public static final String SPREAD_RECYCLE_KEY = "spread:recycle:";

    /**
     * 服务商邀请员工二维码缓存KEY
     */
    public static final String SPREAD_RECYCLE_EMPLOYEE_KEY = "spread:recycle:employee:";

    /**
     * 号码缓存KEY
     */
    public static final String HK_TOKEN_KEY = "hk:token";
}
