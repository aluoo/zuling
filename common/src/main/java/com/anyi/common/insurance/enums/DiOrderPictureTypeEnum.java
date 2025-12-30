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
public enum DiOrderPictureTypeEnum {
    IMG(0, "图片"),
    VIDEO(1, "视频"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}