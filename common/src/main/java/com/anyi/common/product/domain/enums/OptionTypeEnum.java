package com.anyi.common.product.domain.enums;

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
public enum OptionTypeEnum {
    RADIO(1, "单选"),
    CHECKBOX(2, "多选"),
    IMG(3, "图片"),
    VIDEO(4, "视频"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}