package com.anyi.common.cms.domain.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/9/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类ID")
    private Long id;
    @ApiModelProperty("类型")
    private Integer type;
    @ApiModelProperty("业务类型")
    private String bizType;
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("分类图标")
    private String icon;
    @ApiModelProperty("分类描述")
    private String description;
}