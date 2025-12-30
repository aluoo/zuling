package com.anyi.common.insurance.enums;

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
public enum FixOrderOperationEnum {

    CREATE(0, "创建订单", "创建订单"),
    ONE_PASS(1, "创建订单", "创建订单"),
    ONE_FAIL(3, "审核失败,请修改报修资料","审核失败,请修改报修资料"),
    DATA_PASS(4, "资料审核通过,待理赔","资料审核通过,待理赔"),
    DATA_EDIT(5, "资料审核失败,修改维修资料","资料审核失败,修改维修资料"),
    PAY_PASS(6, "理赔完成","理赔完成"),

    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
    @Getter
    private final String remark;
}