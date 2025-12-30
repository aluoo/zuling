package com.anyi.common.product.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("地址信息传输对象")
public class AddressDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("公司ID")
    private Long companyId;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("省市区")
    private String address;
    @ApiModelProperty("详细地址")
    private String detail;
    @ApiModelProperty("联系人")
    private String contact;
    @ApiModelProperty("电话")
    private String phone;
}