package com.anyi.common.account.vo;

import com.anyi.common.util.MoneyUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chenjian
 * @Description
 * @Date 2024/12/3
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerAccountLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long employeeId;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工手机号")
    private String employeePhone;

    @ApiModelProperty("钱包可用余额(分)_变动前")
    private Long ableBalanceBefore;

    @ApiModelProperty("钱包可用余额(分)_变动金额")
    private Long ableBalanceChange;

    @ApiModelProperty("钱包可用余额(分)_变动后")
    private Long ableBalanceAfter;

    @ApiModelProperty("变动说明")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public String getCreateTime(){
        return this.createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getAbleBalanceChange() {
        return  MoneyUtil.convert(this.ableBalanceChange);
    }

    public String getAbleBalanceBefore() {
        return  MoneyUtil.convert(this.ableBalanceBefore);
    }

    public String getAbleBalanceAfter() {
        return  MoneyUtil.convert(this.ableBalanceAfter);
    }


}