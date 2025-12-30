package com.anyi.common.insurance.domain;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.enums.DiOrderPictureTypeEnum;
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
 * @Date 2024/6/5
 * @Copyright
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("di_insurance_order_picture")
@ApiModel(value = "数保订单图片表")
public class DiInsuranceOrderPicture extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("数保订单ID")
    private Long insuranceOrderId;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("地址")
    private String url;
    /**
     * @see DiOrderPictureTypeEnum
     */
    @ApiModelProperty("类型")
    private Integer type;
}