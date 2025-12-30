package com.anyi.common.commission.dto;

import cn.hutool.core.util.NumberUtil;
import com.anyi.common.constant.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 获取能给下级配置的套餐 和能分配的数值
 * </p>
 *
 * @author yan
 * @since 2023-03-07
 */

@Data
@ApiModel(value = "CommissionPlanIssueConf对象", description = "获取能给下级配置的套餐 和能分配的数值")
public class CmPlanPackageDTO implements Serializable {

    @ApiModelProperty("配置表id")
    private Long confId;

    @ApiModelProperty("方案id")
    private Long planId;

    @ApiModelProperty("套餐id")
    private Long packageInfoId;

    @ApiModelProperty("套餐名称")
    private String packageInfoName;

    @ApiModelProperty("上级分成")
    private Long superDivide;
    @ApiModelProperty("佣金方案类型ID")
    private Long bizTypeId;
    @ApiModelProperty("1数值2比例")
    private Integer type;

    public String getSuperDivide() {
        if (superDivide == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(superDivide), 100));
    }
}
