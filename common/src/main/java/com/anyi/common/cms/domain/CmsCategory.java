package com.anyi.common.cms.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@TableName("cms_category")
public class CmsCategory extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("是否显示 0隐藏1显示")
    private Boolean activated;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("业务类型")
    private String bizType;
    @ApiModelProperty("上级分类ID")
    private Long parentId;
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("分类图标")
    private String icon;
    @ApiModelProperty("分类描述")
    private String description;
    @ApiModelProperty("层级路径")
    private String ancestors;
    @ApiModelProperty("层级深度")
    private Integer level;
}