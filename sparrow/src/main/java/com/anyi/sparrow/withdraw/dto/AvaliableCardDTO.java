package com.anyi.sparrow.withdraw.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 账户提现方式绑定
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@ApiModel(value = "WithdrawEmployeeCard对象", description = "账户提现方式绑定")
public class AvaliableCardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long cardId;


    @ApiModelProperty("类型(1-银行卡、2-支付宝、3-对公账户)")
    private Integer type;

    @ApiModelProperty("银行名称")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNo;

    @ApiModelProperty("姓名")
    private String ownerName;

    @ApiModelProperty("身份证号")
    private String idCard;


    @ApiModelProperty("最近使用时间")
    private LocalDateTime latestTime;

    @ApiModelProperty("公司名称(对公)")
    private String companyName;

    @ApiModelProperty("公司税号(对公)")
    private String companyTaxNo;

    @ApiModelProperty("地址(对公)")
    private String address;

    @ApiModelProperty("累计提现信息")
    private String accWithdraw;

    public Long getLatestTime() {
        if (this.latestTime != null) {
            return LocalDateTimeUtil.toEpochMilli(this.latestTime);
        }
        return null;
    }

}
