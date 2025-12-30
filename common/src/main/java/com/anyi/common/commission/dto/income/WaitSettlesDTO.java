package com.anyi.common.commission.dto.income;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.anyi.common.util.MoneyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class WaitSettlesDTO implements Serializable {

    /**
     * 收益说明
     */
    @ApiModelProperty("收益说明")
    private String remark;

    /**
     * 获得时间
     */
    @ApiModelProperty("获得时间")
    private LocalDateTime gainTime;

    /**
     * 金额（元）
     */
    @ApiModelProperty("金额（元）")
    private Long balance;


    public Long getGainTime() {
        return LocalDateTimeUtil.toEpochMilli(this.gainTime);
    }

    public String getBalance() {
        return MoneyUtil.convert(this.balance);
    }

}
