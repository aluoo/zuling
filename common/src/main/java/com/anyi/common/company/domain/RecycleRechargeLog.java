package com.anyi.common.company.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@TableName("recycle_recharge_log")
@ApiModel(value = "服务商充值表")
public class RecycleRechargeLog extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;
    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;
    @ApiModelProperty(value = "回收商Id")
    private Long companyId;
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    @ApiModelProperty(value = "打款凭证")
    private String imageUrl;
    @ApiModelProperty(value = "充值金额")
    private Long rechargeAmount;
    @ApiModelProperty(value = "备注")
    private String remark;
}