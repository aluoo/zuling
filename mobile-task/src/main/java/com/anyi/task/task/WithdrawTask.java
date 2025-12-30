package com.anyi.task.task;

import com.anyi.common.withdraw.service.WithdrawTaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class WithdrawTask {
    @Autowired
    private WithdrawTaskService withdrawTaskService;

    @XxlJob("queryWithdrawPaymentCheck")
    public void queryWithdrawPaymentCheck() {
        XxlJobHelper.log("==========自动查询QFU打款结果并更新提现状态开始========");
        withdrawTaskService.queryPayment();
        XxlJobHelper.log("==========自动查询QFU打款结果并更新提现状态结束========");
    }
}