package com.anyi.commission.service;


import com.anyi.common.commission.domain.CommissionPlan;
import com.anyi.common.commission.domain.CommissionPlanConf;
import com.anyi.common.commission.service.CommissionPlanConfService;
import com.anyi.common.commission.service.CommissionPlanService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.sparrow.SparrowApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 佣金结算测试类
 *
 * @author shenbh
 * @since 2023-11-13
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparrowApplication.class)
public class CommissionPlanTest {
    @Autowired
    private CommissionPlanConfService commissionPlanConfService;
    @Autowired
    private CommissionPlanService commissionPlanService;
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void addPackageId() {
        Long packageId = 7L;

        List<CommissionPlan> planList = commissionPlanService.lambdaQuery()
                .eq(CommissionPlan::getTypeId,2)
                .list();

        System.out.println(planList.size());

        for (CommissionPlan planItem: planList) {
            Long planId = planItem.getId();
            Long employeeId = planItem.getEmployeeId();

            Employee employee  = employeeService.getById(employeeId);

            long count  = commissionPlanConfService.lambdaQuery()
                    .eq(CommissionPlanConf::getPlanId,planId)
                    .eq(CommissionPlanConf::getTypePackageId,packageId)
                    .count();
            System.out.println(count);

            if (count<1){

                CommissionPlanConf planIssueConf = new CommissionPlanConf();
                planIssueConf.setPlanId(planId);
                planIssueConf.setTypePackageId(packageId);
                planIssueConf.setSuperDivide(0L);
                planIssueConf.setChildDivide(0L);
                planIssueConf.setSelfDivide(0L);
                planIssueConf.setSuperScale(BigDecimal.ZERO);
                planIssueConf.setChildScale(BigDecimal.ZERO);
                planIssueConf.setSelfScale(BigDecimal.ZERO);

                planIssueConf.setAncestors(employee.getAncestors());
                planIssueConf.setLevel(employee.getLevel());


                planIssueConf.setCreateTime(new Date());
                planIssueConf.setUpdateTime(new Date());

                //            System.out.println(JSONUtil.toJsonStr(planIssueConf));
                commissionPlanConfService.save(planIssueConf);
            }
        }
    }

}
