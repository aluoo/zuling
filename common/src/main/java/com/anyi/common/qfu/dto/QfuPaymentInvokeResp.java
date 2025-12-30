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
public class QfuPaymentInvokeResp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private String order;

    private String sign;
    private String nonce;
    private Long timestamp;
}