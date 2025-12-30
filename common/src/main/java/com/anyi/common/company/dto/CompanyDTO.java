package com.anyi.common.company.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class CompanyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("商家名称")
    @NotBlank(message = "商家名称不能为空")
    private String name;

    @ApiModelProperty("负责人")
    @NotBlank(message = "负责人不能为空")
    private String contact;

    @ApiModelProperty("联系电话")
    @NotBlank(message = "联系电话不能为空")
    private String contactMobile;

    @ApiModelProperty("省份名称")
    @NotBlank(message = "省份名称不能为空")
    private String province;

    @ApiModelProperty("城市名称")
    @NotBlank(message = "城市名称不能为空")
    private String city;

    @ApiModelProperty("区名称")
    @NotBlank(message = "区名称不能为空")
    private String region;

    @ApiModelProperty("详细地址")
    @NotBlank(message = "详细地址不能为空")
    private String address;

    @ApiModelProperty("省编码")
    @NotBlank(message = "省编码不能为空")
    private String provinceCode;

    @ApiModelProperty("城市编码")
    @NotBlank(message = "城市编码不能为空")
    private String cityCode;

    @ApiModelProperty("区编码")
    @NotBlank(message = "区编码不能为空")
    private String regionCode;

    @ApiModelProperty("门店照片")
    @NotBlank(message = "门店照片")
    private String frontUrl;
    @ApiModelProperty("营业执照")
    @NotBlank(message = "营业执照不能为空")
    private String busLicense;

    private Long userId;

    @ApiModelProperty("邀请员工ID")
    @NotNull(message = "邀请员工ID不能为空")
    private Long aplId;

    @ApiModelProperty("2门店3连锁店4服务商5租机商")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty("3换机模式4一键更新")
    private Integer exchangeType;


}