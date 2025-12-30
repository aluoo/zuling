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
 * 打款记录
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("withdraw_trans_log")
@ApiModel(value = "WithdrawTransLog对象", description = "打款记录")
public class WithdrawTransLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long applyId;

    @ApiModelProperty("打款类型(1-全款、2-首款、3-尾款)")
    private Integer transType;

    private Long amount;

    private Long sysUserId;

    private String transNo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
