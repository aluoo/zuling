package com.anyi.sparrow.withdraw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 提现审核记录
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("withdraw_check_log")
@ApiModel(value = "WithdrawCheckLog对象", description = "提现审核记录")
public class WithdrawCheckLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("提现申请id")
    private Long applyId;

    @ApiModelProperty("审核人id")
    private Long sysUserId;

    @ApiModelProperty("审核人id")
    private Long employeeId;

    @ApiModelProperty("审核前状态")
    private Integer oldStatus;

    @ApiModelProperty("审核后状态")
    private Integer newStatus;

    @ApiModelProperty("审核备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
