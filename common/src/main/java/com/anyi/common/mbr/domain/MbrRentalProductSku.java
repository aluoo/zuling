package com.anyi.common.mbr.domain;

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
 * <p>
 * 
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mbr_rental_product_sku")
@ApiModel(value = "租机SKU表")
@Data
public class MbrRentalProductSku extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("是否上架 0否1是")
    private Boolean activated;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("品牌ID")
    private Long brandId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("规格名称")
    private String spec;

    @ApiModelProperty("零售价")
    private Integer retailPrice;



}