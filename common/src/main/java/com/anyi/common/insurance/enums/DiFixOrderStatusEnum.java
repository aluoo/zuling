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
public enum DiFixOrderStatusEnum {
    // 提交待审核
    WAIT(0, "履约申请待审核"),
    DATA_WAIT(1, "履约资料审核中"),
    ONE_PASS(2, "审核通过,待上传履约资料"),
    ONE_FAIL(3, "审核失败,修改履约申请资料"),
    DATA_PASS(4, "审核通过,待履约"),
    DATA_EDIT(5, "审核失败,请修改履约资料"),
    PAY_PASS(6, "已履约"),

    CANCEL(7, "履约申请取消"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}