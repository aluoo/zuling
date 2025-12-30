package com.anyi.common.commission.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("commission_settle_data_daily_detail")
@ApiModel(value = "CommissionSettleDataDailyDetail对象", description = "佣金统计每日基础详情表")
public class CommissionSettleDataDailyDetail extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("每天0点")
    private Date day;

    @ApiModelProperty("结算业务类型(1-推广、2-服务费、3-奖金)")
    private Integer bizType;

    @ApiModelProperty("获得类型")
    private Integer gainType;

    @ApiModelProperty("统计金额(分)")
    private Integer value;

    @ApiModelProperty("备注说明")
    private String remark;

    @ApiModelProperty("佣金统计每日基础表ID")
    private Long dailyBaseId;
}