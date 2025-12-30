package com.anyi.common.commission.domain;

import com.anyi.common.commission.dto.RuleVersionDTO;
import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单表佣金结算关系表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-29
 */
@Getter
@Setter
@TableName(value = "order_commission_rule", autoResultMap = true)
@ApiModel(value = "OrderCommissionRule对象", description = "订单表佣金结算关系表")
public class OrderCommissionRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("分佣方案id")
    private Long commissionType;

    @ApiModelProperty("分佣套餐id")
    private Long commissionPackage;

    @ApiModelProperty("规则版本")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<RuleVersionDTO> ruleVersion;

//    @ApiModelProperty("创建时间")
//    private LocalDateTime createTime;
//
//    @ApiModelProperty("更新时间")
//    private LocalDateTime updateTime;


}
