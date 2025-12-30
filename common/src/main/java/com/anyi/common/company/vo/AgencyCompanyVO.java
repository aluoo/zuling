package com.anyi.common.company.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyCompanyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店ID")
    private Long id;
    @ApiModelProperty("门店名称")
    private String name;
    @ApiModelProperty("门店类型2单片3连锁4服务商")
    private Integer type;
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
    private Integer score;
    @ApiModelProperty("连锁标识")
    private Boolean chainFlag;
    @ApiModelProperty("营业执照")
    private String busLicense;
    @ApiModelProperty("员工ID")
    private Long employeeId;
    @ApiModelProperty("员工层级")
    private String ancestors;

}
