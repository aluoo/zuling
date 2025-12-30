package com.anyi.sparrow.hk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/15
 */
@AllArgsConstructor
public enum HkOrderStatusEnum {
    ONE(1, "待处理"),
    THREE(3, "下单成功"),
    FOUR(4, "已写卡"),
    FIVE(5, "已发货"),
    SIX(6, "已签收"),
    SEVEN(7, "已激活"),
    EIGHT(8, "激活失败"),
    NINE(9, "失败订单"),
    TEN(10, "提单失败"),
    TWELVE(12, "订购失败"),
    THIRTEEN(13, "已拒收"),
    SIXTEEN(16, "已充值(大于100)"),
    SEVENTEEN(17, "已充值(大于50)"),
    TWENTY(20, "用户撤单"),
    UNKNOWN(50, "未知状态"),
    EIGHTEEN(18, "已充值(小于50)");

    @Getter
    private final Integer code;
    @Getter
    private final String name;
}