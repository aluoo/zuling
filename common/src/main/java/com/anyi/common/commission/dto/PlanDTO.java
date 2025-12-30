package com.anyi.common.commission.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 佣金方案表
 * </p>
 *
 * @author yan
 * @since 2023-03-07
 */
@Data
public class PlanDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("方案Id")
    private Long planId;

    @ApiModelProperty("方案名称")
    private String planName;

    @ApiModelProperty("佣金方案类型ID")
    private Long bizTypeId;

    @ApiModelProperty("员工数量")
    private int members;


}
