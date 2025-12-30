package com.anyi.sparrow.withdraw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/3/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PublicAccountBindReq {

    @ApiModelProperty(value = "发卡银行")
    @NotEmpty(message = "发卡银行名称不能为空")
    private String accountName;
    @NotEmpty(message = "卡号不能为空")
//    @Pattern(regexp = "^(\\d{16}|\\d{19})$",message = "卡号格式不正确")
    @ApiModelProperty(value = "卡号")
    private String accountNo;
    @NotEmpty(message = "公司名不能为空")
    @ApiModelProperty(value = "公司名")
    private String companyName;
    @NotEmpty(message = "税号不能为空")
    @ApiModelProperty(value = "税号")
    private String companyTaxNo;

    @NotEmpty(message = "公司地址不能为空")
    @ApiModelProperty(value = "公司地址")
    private String address;
}
