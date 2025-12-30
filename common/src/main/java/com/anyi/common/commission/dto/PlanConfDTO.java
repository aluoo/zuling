package com.anyi.common.commission.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author shenbh
 * @since 2023/3/29
 */
@Data
public class PlanConfDTO implements Serializable {

    private Long planId;

    @ApiModelProperty("上级分成数值")
    private Long superDivide;

    @ApiModelProperty("下级分成数值")
    private Long childDivide;

    @ApiModelProperty("自身分成数值")
    private Long selfDivide;

    @ApiModelProperty("上级分成比例")
    private BigDecimal superScale;

    @ApiModelProperty("下级分成比例")
    private BigDecimal childScale;

    @ApiModelProperty("自收分成比例")
    private BigDecimal selfScale;
}
