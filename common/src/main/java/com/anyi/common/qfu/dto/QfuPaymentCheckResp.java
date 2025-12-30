package com.anyi.common.qfu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QfuPaymentCheckResp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    /**
     * code 0 正在打款
     * code 1 打款成功
     * code -1 打款失败
     * code -2 钱包余额不足
     * code -3 渠道余额不足
     * code -4 单号不存在（建议转人工排查）
     * code -5 业务校验失败
     * code -6 钱包已被锁定
     * code -7 审批超时关闭
     * code -8 渠道停机升级
     * code -9 用户证件已被锁定
     * code -999 系统出错（请凭原单号重试，或联系平台处理，不能看作失败！）
     */
    private String message;

    private String order;

    private String sign;
    private String nonce;
    private Long timestamp;
}