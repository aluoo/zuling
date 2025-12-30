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
 * 数保产品选项表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Getter
@Setter
@TableName("di_option")
@ApiModel(value = "DiOption对象", description = "数保产品选项表")
public class DiOption extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("1单选 2多选 3图片")
    private Integer type;

    @ApiModelProperty("是否必填0否1是")
    private Boolean required;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("上级ID")
    private Long parentId;

    @ApiModelProperty("层级路径")
    private String ancestors;

    @ApiModelProperty("示例图")
    private String sampleImage;

    @ApiModelProperty("选项code")
    private String code;
}
