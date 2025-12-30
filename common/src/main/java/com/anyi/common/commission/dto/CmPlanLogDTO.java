package com.anyi.common.commission.dto;


import com.anyi.common.commission.domain.CommissionPlan;
import com.anyi.common.commission.domain.CommissionPlanConf;
import com.anyi.common.commission.domain.CommissionPlanMembers;
import lombok.Data;

import java.util.List;

@Data
public class CmPlanLogDTO {
    private CommissionPlan plan;

    private List<CommissionPlanMembers> members;

    private List<CommissionPlanConf> issueConfs;
}
