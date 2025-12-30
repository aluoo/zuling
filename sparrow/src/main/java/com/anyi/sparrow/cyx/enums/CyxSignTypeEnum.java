package com.anyi.sparrow.cyx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@AllArgsConstructor
public enum CyxSignTypeEnum {
    ABC("990136", "中国农业银行"),
    CCB("990135", "中国建设银行"),
    WECHAT("99011525968241", "微信"),
    ALIPAY("99012088031075279400", "支付宝"),
    UNIVERSAL_BANK_CARD("990132", "通用银行卡"),
    PSBC("990138", "邮储银行"),
    ;

    @Getter
    private final String code;
    @Getter
    private final String desc;
}