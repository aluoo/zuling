package com.anyi.common.product.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mb_product_sku")
@ApiModel(value = "手机商品sku表")
public class ProductSku extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;

    @ApiModelProperty(value = "是否上架")
    private Boolean activated;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "规格名称")
    private String spec;
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "品牌ID")
    private Long brandId;
    @ApiModelProperty(value = "手机商品ID")
    private Long productId;
    @ApiModelProperty(value = "市场零售价")
    private Integer retailPrice;
}