package com.anyi.sparrow.applet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author peng can
 * @date 2022/12/14
 */
@AllArgsConstructor
@Getter
public enum AgreementOrderStatus {

    UNAUTHORIZED(0, "未开通"),
    PAUSED(0, "已暂停车主服务"), //微信查询的状态
    OVERDUE(5, "已开通车主服务，有欠费"), //微信查询的状态
    NORMAL(5, "已开通")
    ;

    Integer status;

    String name;
}
