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
public enum DiOrderInsuranceStatusEnum {
    /**
     * 待上传
     */
    PENDING_UPLOAD(0, "未上传"),
    /**
     * 待出单
     */
    PENDING_EFFECTIVE(1, "未出单"),
    /**
     * 已出单
     */
    EFFECTIVE_FINISHED(2, "已出单"),
    ;

    @Getter
    private final Integer code;
    @Getter
    private final String desc;
}