package com.anyi.common.mobileStat.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 回收商统计日看板表
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Getter
@Setter
@TableName("recycle_data_daily_base")
@ApiModel(value = "RecycleDataDailyBase对象", description = "回收商统计日看板表")
public class RecycleDataDailyBase extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("交易金额")
    private Long transAmount;

    @ApiModelProperty("交易数量")
    private Integer transNum;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("出价次数")
    private Integer quotePriceNum;

    @ApiModelProperty("报价时长(毫秒)")
    private Long quoteTimeSpent;

    @ApiModelProperty("确认收货量")
    private Integer orderConfirmNum;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("每天0点")
    private Date day;
}
