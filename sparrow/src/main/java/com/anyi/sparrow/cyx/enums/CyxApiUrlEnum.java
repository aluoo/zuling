package com.anyi.sparrow.cyx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@AllArgsConstructor
public enum CyxApiUrlEnum {
    UNIQUE_CHECK("/traffic/vehicle/unique_check", "P0gmB1dg", "唯一性校验接口"),
    ACTIVE_CALLBACK_QUERY_TEST("/etc/extra/etcEnisActiveCallBackQueryZT", "FX8VI2Ls", "etc设备激活回调查询接口"),
    ACTIVE_CALLBACK_QUERY_PROD("/etc-issue-enis-service-web/etc/extra/etcEnisActiveCallBackQueryZT", "FX8VI2Ls", "etc设备激活回调查询接口"),
    ;

    @Getter
    private final String url;
    @Getter
    private final String apiAppId;
    @Getter
    private final String desc;
}