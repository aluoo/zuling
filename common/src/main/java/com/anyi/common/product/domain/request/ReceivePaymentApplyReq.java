package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/4
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户申请收款信息请求对象")
public class ReceivePaymentApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("报价订单号")
    @NotNull(message = "订单号不能为空")
    private Long id;
    @ApiModelProperty("收款人姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @ApiModelProperty("收款人手机")
    @NotBlank(message = "手机不能为空")
    private String mobile;
    @ApiModelProperty("收款人身份证")
    @NotBlank(message = "身份证不能为空")
    private String idCard;
}