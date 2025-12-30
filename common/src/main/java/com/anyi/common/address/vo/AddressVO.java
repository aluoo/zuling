package com.anyi.common.address.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AddressVO {
    @ApiModelProperty("地址id（用于更新接口）")
    private Long id;

    @ApiModelProperty("省市区")
    private String address;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;
}
