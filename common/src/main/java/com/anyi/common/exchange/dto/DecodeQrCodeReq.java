package com.anyi.common.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/15
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecodeQrCodeReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "图片地址不能为空")
    private String url;
}