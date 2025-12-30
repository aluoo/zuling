package com.anyi.common.insurance.domain;

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
 * 险种类型表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_type")
@ApiModel(value = "DiType对象", description = "险种类型表")
public class DiType extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("产品类型名称")
    private String name;

    @ApiModelProperty("是否套餐")
    private Boolean packageAble;

    @ApiModelProperty("1正常2下线3删除")
    private Integer status;

}
