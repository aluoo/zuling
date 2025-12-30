package com.anyi.task.task;

import com.anyi.common.withdraw.service.WithdrawTaskService;
import com.anyi.task.MobileTaskApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WithdrawTaskTest extends MobileTaskApplicationTest {
    @Autowired
    WithdrawTaskService withdrawTaskService;

    @Test
    public void queryPayment() {
        withdrawTaskService.queryPayment();
    }
}