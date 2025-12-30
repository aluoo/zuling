package com.anyi.common.product.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("选项结构信息对象")
public class OptionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("类型 1单选 2多选 3图片")
    private Integer type;
    @ApiModelProperty("是否必填")
    private Boolean required;
    @ApiModelProperty("层级")
    private Integer level;
    @ApiModelProperty("上级ID")
    private Long parentId;
    @ApiModelProperty("层级路径")
    private String ancestors;
    @ApiModelProperty("是否选中")
    private Boolean checked;
    @ApiModelProperty("示例图")
    private String sampleImage;
    @ApiModelProperty("选项code")
    private String code;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("下级列表")
    private List<OptionDTO> children;
}