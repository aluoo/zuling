package com.anyi.common.withdraw.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.qfu.WithdrawPaymentCheckService;
import com.anyi.common.withdraw.domain.CommonWithdrawEmployeeApply;
import com.anyi.common.withdraw.domain.enums.WithdrawApplyStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class WithdrawTaskService {
    @Autowired
    private CommonWithdrawEmployeeApplyService withdrawEmployeeApplyService;
    @Autowired
    WithdrawPaymentCheckService withdrawPaymentCheckService;

    public void queryPayment() {
        // 找出打款中的申请
        List<CommonWithdrawEmployeeApply> list = withdrawEmployeeApplyService.lambdaQuery()
                .eq(CommonWithdrawEmployeeApply::getStatus, WithdrawApplyStatusEnum.paying.getType())
                .list();
        if (CollUtil.isEmpty(list)) {
            return;
        }

        list.forEach(withdrawPaymentCheckService::paymentCheck);
    }
}