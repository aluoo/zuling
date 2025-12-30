package com.anyi.common.cms.domain;

import com.anyi.common.cms.domain.enums.IconPlaceEnum;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@TableName("cms_index_icon")
public class CmsIndexIcon extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("是否显示 0隐藏1显示")
    private Boolean activated;
    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * @see IconPlaceEnum
     */
    @ApiModelProperty("展示位置")
    private Integer place;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("跳转路径")
    private String linkUrl;
}