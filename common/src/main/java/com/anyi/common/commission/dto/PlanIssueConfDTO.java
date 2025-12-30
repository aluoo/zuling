package com.anyi.common.commission.dto;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 发行佣金配置表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Data
@ApiModel(value = "CommissionPlanIssueConf对象", description = "佣金配置表")
public class PlanIssueConfDTO {

    @ApiModelProperty("配置表id")
    private Long confId;
    @ApiModelProperty("1数值2比例")
    private Integer type;
    @ApiModelProperty("套餐id")
    private Long packageInfoId;

    @ApiModelProperty("套餐id")
    private String packageInfoName;

    @ApiModelProperty("上级分成")
    private Long superDivide;

    @ApiModelProperty("下级分成")
    private Long childDivide;

    @ApiModelProperty("自收分成")
    private Long selfDivide;

    @ApiModelProperty("上级分成比例")
    private BigDecimal superScale;

    @ApiModelProperty("下级分成比例")
    private BigDecimal childScale;

    @ApiModelProperty("自收分成比例")
    private BigDecimal selfScale;


    public String getSuperDivide() {
        if (superDivide == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(superDivide), 100));
    }

    public String getChildDivide() {
        if (childDivide == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(childDivide), 100));
    }

    public String getSelfDivide() {
        if (selfDivide == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(selfDivide), 100));
    }

    public String getSuperScale() {
        return NumberUtil.decimalFormat("0.00", NumberUtil.mul(superScale, 100));
    }

    public String getChildScale() {
        return NumberUtil.decimalFormat("0.00", NumberUtil.mul(childScale, 100));
    }

    public String getSelfScale() {
        return NumberUtil.decimalFormat("0.00", NumberUtil.mul(selfScale, 100));
    }

}
