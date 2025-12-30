package com.anyi.common.insurance.response;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 拉新安装包
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Data
public class InsuranceAccountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("金额")
    private Long amount;

    public String getAmount() {
        if (amount == null) {
            return "";
        }
        return NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(amount), 100));
    }


}
