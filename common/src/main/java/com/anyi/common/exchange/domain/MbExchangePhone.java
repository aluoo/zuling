package com.anyi.common.exchange.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 拉新换机包
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Getter
@Setter
@TableName("mb_exchange_phone")
@ApiModel(value = "MbExchangePhone对象", description = "拉新换机包")
public class MbExchangePhone extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("换机包名称")
    private String name;

    @ApiModelProperty("使用场景")
    private Integer type;

    @ApiModelProperty("包编码")
    private String channelNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态")
    private Integer status;

}
