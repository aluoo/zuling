package com.anyi.common.product.domain.response;

import cn.hutool.core.lang.tree.Tree;
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
 * @Date 2024/1/31
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品详情响应对象")
public class ProductDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    private Long id;
    @ApiModelProperty("商品ID")
    private Long productId;
    @ApiModelProperty("商品名称")
    private String name;
    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("分类信息")
    private List<CategoryVO> categories;

    @ApiModelProperty("选项信息")
    private List<Tree<Long>> optionals;

    @ApiModelProperty("备注信息")
    private List<String> remarks;

    @ApiModelProperty("旧订单的备注信息")
    private String existRemarks;
}