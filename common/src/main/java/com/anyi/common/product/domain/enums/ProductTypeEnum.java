package com.anyi.common.product.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description 手机系统类型枚举类
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ProductTypeEnum {
    ANDROID(0, "Android"),
    IOS(1, "iOS"),
    ;

    private final Integer code;
    private final String desc;
}