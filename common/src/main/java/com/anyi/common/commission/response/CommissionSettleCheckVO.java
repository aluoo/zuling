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
public class CommissionSettleCheckVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("业务类型")
    private Integer commissionType;

    @ApiModelProperty("结算时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date settleTime;

    @ApiModelProperty("团队业绩")
    private Long settleBalance;

    @ApiModelProperty("区域经理")
    private String regionName;


    public String getCommissionType(){
        return EnumUtil.getBy(CommissionBizType::getType,Long.valueOf(commissionType)).getTypeName();
    }

    public String getSettleBalance() {
        return (this.settleBalance >= 0 ? "+" : "") + MoneyUtil.convert(this.settleBalance);
    }

}