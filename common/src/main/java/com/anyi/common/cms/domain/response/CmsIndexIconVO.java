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
 * @Date 2024/5/17
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmsIndexIconVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("跳转路径")
    private String linkUrl;

    private Boolean isTest;
}