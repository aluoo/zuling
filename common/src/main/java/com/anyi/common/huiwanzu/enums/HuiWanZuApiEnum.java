package com.anyi.common.huiwanzu.enums;

import cn.hutool.http.ContentType;
import cn.hutool.http.Method;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/5/15
 * @Copyright
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum HuiWanZuApiEnum {
    SHOPS_CREATE("/open/shops/create", "创建店铺接口", Method.POST, ContentType.JSON),
    ORDERS_GET_BY_ID("/open/orders/getById", "查询订单接口", Method.POST, ContentType.JSON),
    SHOPS_UPDATE("/open/shops/update", "店铺补充资料", Method.POST, ContentType.JSON),
    ;

    private final String path;
    private final String desc;
    private final Method method;
    private final ContentType contentType;
}