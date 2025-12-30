package com.anyi.common.commission.response;

import cn.hutool.core.util.EnumUtil;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionSettleCheckSumVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数保交易")
    private Long insuranceTotal;

    @ApiModelProperty("app拉新")
    private Long appTotal;

    @ApiModelProperty("二手交易")
    private Long mobileTotal;


    public String getInsuranceTotal() {
        return  MoneyUtil.convert(this.insuranceTotal);
    }

    public String getAppTotal() {
        return  MoneyUtil.convert(this.appTotal);
    }

    public String getMobileTotal() {
        return  MoneyUtil.convert(this.mobileTotal);
    }

}