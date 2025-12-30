package com.anyi.common.jdl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description 下单来源
 * @Date 2024/3/26
 * @Copyright
 * @Version 1.0
 * @link <a href="https://cloud.jdl.com/#/open-business-document/access-guide/267/54152">下单来源</a>
 */
@AllArgsConstructor
public enum OrderOriginEnum {
    C2C(0, "快递C2C"),
    B2C(1, "快递B2C"),
    C2B(2, "C2B"),
    KY_B2C(4, "快运B2C"),
    KY_C2C(5, "快运C2C"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}