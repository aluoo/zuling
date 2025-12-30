package com.anyi.sparrow.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 可提现首页页面接口
 *
 * @author shenbh
 * @since 2023/3/23
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class AmountTaxDTO implements Serializable {

    @ApiModelProperty(value = "提交金额(分)", dataType = "Long", example = "60035")
    private Long amount;

    @ApiModelProperty(value = "代缴税额(分)", dataType = "Long", example = "4082")
    private Long taxAmount;

    private Boolean autoPayment;
}