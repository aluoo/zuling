package com.anyi.common.insurance.response;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.anyi.common.account.constant.UserFocusTypeEnum;
import com.anyi.common.insurance.constant.CompanyUserFocusTypeEnum;
import com.anyi.common.util.MoneyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAccountLogDTO implements Serializable {
    //   {
//            "ableBalanceChange": "-100.00",
//            "ableBalanceChangeAfter": "900.00",
//            "createTime": 1679452174935,
//            "title": "提现",
//            "typeCode": "withdraw",
//            "correlationId": 1111
//        },
    @ApiModelProperty(value = "变更金额", example = "-100.00")
    private Long changeBalance;

    @ApiModelProperty(value = "可用金额变更金额", example = "-100.00")
    private Long ableBalanceChange;

    @ApiModelProperty(value = "变更后余额", example = "100.00")
    private Long ableBalanceChangeAfter;
    @ApiModelProperty(value = "变动时间", example = "1679452174935")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "变动说明", example = "提现")
    private String title;

    private int changeDetailType;

    @ApiModelProperty("主变动类型(收入、支出、不变)")
    private Integer changeMainType;


    @ApiModelProperty(value = "关联业务编码", example = "withdraw")
    private String typeCode;
    @ApiModelProperty(value = "关联ID", example = "-100.00")
    private Long correlationId;
    @ApiModelProperty(value = "资金记录ID")
    private Long logId;


    public void setChangeDetailType(int changeDetailType) {
        this.changeDetailType = changeDetailType;
    }


    public Long getCreateTime() {
        return LocalDateTimeUtil.toEpochMilli(this.createTime);
    }


    public String getTypeCode() {

        CompanyUserFocusTypeEnum userFocusTypeEnum = CompanyUserFocusTypeEnum.findUserFocusTypeBy(this.changeDetailType);
        if (userFocusTypeEnum!=null){
            return userFocusTypeEnum.name();
        }


        return null;
    }


    public String getAbleBalanceChange() {
        return (this.ableBalanceChange >= 0 ? "+" : "") + MoneyUtil.convert(this.ableBalanceChange);
    }

    public String getChangeBalance() {
        return (this.changeMainType ==2 ? "+" : "-") + MoneyUtil.convert(this.changeBalance);
    }

    public String getAbleBalanceChangeAfter() {
        return  (this.ableBalanceChangeAfter >= 0 ? "" : "-") + MoneyUtil.convert(this.ableBalanceChangeAfter);
    }
}
