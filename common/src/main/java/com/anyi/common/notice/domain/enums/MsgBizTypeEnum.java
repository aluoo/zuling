package com.anyi.common.notice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/15
 */
@AllArgsConstructor
public enum MsgBizTypeEnum {

    COMM("comm", "普通消息", "普通"),
    SYSNOTICE("sysnotice", "系统公告", "公告"),
    WITHDRAW("withdraw", "提现", "提现"),
    ACTIVE_APPEAL("active_appeal", "激活申诉", "申诉"),
    DEPOSIT_APPEAL("deposit_appeal", "押金申诉", "申诉"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
    @Getter
    private final String type;

    public static String getTypeByCode(String code) {
        for (MsgBizTypeEnum typeEnum : MsgBizTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getType();
            }
        }
        return "";
    }
}