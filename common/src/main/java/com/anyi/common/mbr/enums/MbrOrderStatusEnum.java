package com.anyi.common.mbr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@AllArgsConstructor
public enum MbrOrderStatusEnum {

    ORDER_CREATE(0, "订单创建"),
    CHECK_PASS(1, "审核通过"),
    REFUSE(2, "审核拒绝"),
    LOCKING(3, "上锁中"),
    FINISHED(4, "已完成"),
    REJECT(10, "已拒绝"),
    ABNORMAL(11, "异常"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}