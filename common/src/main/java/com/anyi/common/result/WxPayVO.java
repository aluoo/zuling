package com.anyi.common.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WxPayVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("appid")
    private String appId;

    @ApiModelProperty("timeStamp")
    private String timeStamp;

    @ApiModelProperty("nonceStr")
    private String nonceStr;

    @ApiModelProperty("prePackage, 小程序支付的参数需要命名为package")
    private String prePackage;

    @ApiModelProperty("packageValue, 小程序支付的参数需要命名为package")
    private String packageValue;

    @ApiModelProperty("signType")
    private String signType;

    @ApiModelProperty("paySign")
    private String paySign;
}