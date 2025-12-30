package com.anyi.common.product.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/31
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品查询请求对象")
public class ProductQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "一级分类ID", required = true, notes = "关键词为空时一级分类ID必传")
    // @NotNull(message = "一级分类ID不能为空")
    private Long parentId;

    @ApiModelProperty(value = "二级分类ID，不传则列出一级分类下所有商品")
    private Long categoryId;

    @ApiModelProperty(value = "品牌/型号搜索关键词")
    private String keyword;
}