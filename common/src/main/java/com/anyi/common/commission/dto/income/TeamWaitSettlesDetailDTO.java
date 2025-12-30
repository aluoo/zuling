package com.anyi.common.commission.dto.income;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.anyi.common.util.MoneyUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class TeamWaitSettlesDetailDTO {

    /**
     * 员工名
     */
    @ApiModelProperty("员工名")
    private String name;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 是否是部门管理员
     */
    @ApiModelProperty("是否是部门管理员: true-是 ,false-否 ")
    private boolean deptFlag;

    /**
     * 金额（元）
     */
    @ApiModelProperty("金额（元）")
    private Long balance;

    @ApiModelProperty("获得时间")
    private LocalDateTime gainTime;

    public Long getGainTime() {
        return LocalDateTimeUtil.toEpochMilli(this.gainTime);
    }

    public String getBalance() {
        return MoneyUtil.convert(this.balance);
    }

}