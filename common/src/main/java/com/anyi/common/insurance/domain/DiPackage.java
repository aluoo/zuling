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
 * 保险套餐表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_package")
@ApiModel(value = "DiPackage对象", description = "保险套餐表")
public class DiPackage extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("险种ID")
    private Long typeId;

    @ApiModelProperty("套餐名称")
    private String name;

    @ApiModelProperty("1正常2下线3删除")
    private Integer status;

}
