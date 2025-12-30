package com.anyi.common.hk.domain;

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
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("hk_product")
public class HkProduct extends AbstractBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "运营商ID")
    private Long operatorId;
    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "状态 0下架 1上架")
    private Integer status;

    @ApiModelProperty(value = "价格 单位分")
    private Long price;
    @ApiModelProperty(value = "是否选号 0否1是")
    private Boolean requireSelectMobile;

    @ApiModelProperty(value = "分佣条件（订单状态）")
    private Long commissionStatus;

    @ApiModelProperty(value = "分佣方案套餐")
    private Long commissionTypePackageId;

    @ApiModelProperty(value = "列表主图")
    private String masterImage;
    @ApiModelProperty(value = "详情图")
    private String detailImage;
    @ApiModelProperty(value = "卖点1")
    private String sellPointOne;
    @ApiModelProperty(value = "卖点2")
    private String sellPointTwo;
    @ApiModelProperty(value = "卖点3")
    private String sellPointThree;
    @ApiModelProperty(value = "标签1")
    private String tagOne;
    @ApiModelProperty(value = "标签2")
    private String tagTwo;
    @ApiModelProperty(value = "标签3")
    private String tagThree;
}