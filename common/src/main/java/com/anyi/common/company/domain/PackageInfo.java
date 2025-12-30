package com.anyi.common.company.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 套餐信息
 * </p>
 *
 * @author L
 * @since 2024-02-26
 */
@Getter
@Setter
@TableName("package_info")
@ApiModel(value = "PackageInfo对象", description = "套餐信息")
public class PackageInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("佣金方案类型ID")
    private Long bizTypeId;

    @ApiModelProperty("门店ID")
    private Long companyId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("套餐编码")
    private String code;

    @ApiModelProperty("平台补贴")
    private Long platformSubsidyPrice;

    @ApiModelProperty("默认分佣比例")
    private BigDecimal platCommissionScale;

    @ApiModelProperty("默认最大分佣金额(单位：分)")
    private Long platCommissionFee;

    @ApiModelProperty("实际分佣比例")
    private BigDecimal realCommissionScale;

    @ApiModelProperty("实际最大分佣金额(单位：分)")
    private Long realCommissionFee;

    @ApiModelProperty("价格区间下限(单位：分)")
    private Long priceLow;

    @ApiModelProperty("价格区间上限(单位：分)")
    private Long priceHigh;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;

    @ApiModelProperty("排序号")
    private Integer orderNo;
}