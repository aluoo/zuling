package com.anyi.common.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 手机-数保产品关联表
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("di_product_insurance")
@ApiModel(value = "DiProductInsurance对象", description = "手机-数保产品关联表")
public class DiProductInsurance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("数保产品ID")
    private Long insuranceId;

    @ApiModelProperty("是否删除0否1是")
    private Boolean deleted;

    @ApiModelProperty("数保产品上下线标识0否1是")
    private Boolean activated;
}