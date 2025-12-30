package com.anyi.sparrow.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author shenbh
 * @since 2023-03-30
 */
@Getter
@Setter
@ApiModel(value = "提现详情对象", description = "提现详情对象")
public class ApplyDetailDTO implements Serializable {

    @ApiModelProperty("类型(1-银行卡、2-支付宝、3-对公账户)")
    private Integer type;

    @ApiModelProperty(value = "提交金额")
    private String amount;

    @ApiModelProperty("代扣税额")
    private String taxAmount;

    @ApiModelProperty("银行名称")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNo;

    @ApiModelProperty("姓名")
    private String ownerName;

    @ApiModelProperty("公司名称(对公)")
    private String companyName;


    private List<ApplyStageDTO> stage;


}
