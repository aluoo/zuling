package com.anyi.common.company.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecycleCompanyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店ID")
    private Long id;
    @ApiModelProperty("门店名称")
    private String name;
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("区")
    private String region;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("门店照片")
    private String frontUrl;
    @ApiModelProperty("门店状态")
    private Integer status;
    @ApiModelProperty("门店状态中文")
    private String statusName;
    @ApiModelProperty("联系人")
    private String contact;
    @ApiModelProperty("联系电话")
    private String contactMobile;
    @ApiModelProperty("评分")
    private int score;
    @ApiModelProperty("员工ID")
    private Long employeeId;


}
