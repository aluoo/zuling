package com.anyi.common.company.dto;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 套餐信息
 * </p>
 *
 * @author L
 * @since 2024-02-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * @see com.anyi.common.commission.enums.CommissionBizType
     */
    @ApiModelProperty("佣金方案类型ID")
    private Long bizTypeId;

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

    @ApiModelProperty("实际分佣比例")
    private String realCommissionScaleStr;

    @ApiModelProperty("实际最大分佣金额(单位：元)")
    private Long realCommissionFeeStr;

    @ApiModelProperty("价格区间下限(单位：分)")
    private Long priceLow;

    @ApiModelProperty("价格区间上限(单位：分)")
    private Long priceHigh;

    @ApiModelProperty("1启用 0禁用")
    private Integer status;

    @ApiModelProperty("排序号")
    private Integer orderNo;

    private Date createTime;

    private Date updateTime;

    private Integer price;

    private Long companyId;

    public String getPlatCommissionFee() {
        if (platCommissionFee == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(platCommissionFee), 100));
    }

    public String getPlatCommissionScale() {
        if (platCommissionScale == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.mul(platCommissionScale, 100));
    }

    public String getRealCommissionFeeStr() {
        if (realCommissionFee == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(realCommissionFee), 100));
    }

    public String getRealCommissionScaleStr() {
        if (realCommissionScale == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.mul(realCommissionScale, 100));
    }
}