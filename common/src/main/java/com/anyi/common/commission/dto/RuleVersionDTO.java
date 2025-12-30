package com.anyi.common.commission.dto;

import lombok.Data;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/3/29
 */
@Data
public class RuleVersionDTO {

    private Long employeeId;

    private Long childEmployeeId;

    private int level;

    private String ancestors;

    private PlanConfDTO planConf;

    private CommissionDTO commission;


}
