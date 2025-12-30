package com.anyi.common.company.dto;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
public class PackageInfoApplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull
    private Long id;

    @ApiModelProperty("实际分佣比例范围0-100")
    @NotNull
    private BigDecimal realCommissionScale;

    @ApiModelProperty("实际最大分佣金额(单位：分)范围0-100")
    @NotNull
    private BigDecimal realCommissionFee;

    public Long getRealCommissionFee() {
        if (realCommissionFee == null) {
            return 0L;
        }
        return NumberUtil.mul(realCommissionFee, 100).toBigInteger().longValue();
    }

    public BigDecimal getRealCommissionScale() {
        if (realCommissionScale == null) {
            return BigDecimal.ZERO;
        }
        return NumberUtil.div(realCommissionScale, 100);
    }

}
