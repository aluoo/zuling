package com.anyi.sparrow.hk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/7/28
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyOrderReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品编码")
    private String fetchCode;
    @ApiModelProperty(value = "第三方订单号",hidden = true)
    private String thirdOrderSn;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("身份证")
    private String idCard;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("省份")
    private String provinceName;
    @ApiModelProperty("城市")
    private String cityName;
    @ApiModelProperty("区")
    private String townName;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("预约号码，选号业务才需要传")
    private String planMobileNumber;

}