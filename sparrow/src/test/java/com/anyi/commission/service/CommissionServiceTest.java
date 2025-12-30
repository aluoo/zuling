package com.anyi.commission.service;

import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.sparrow.SparrowApplication;
import com.anyi.sparrow.SparrowApplicationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/30
 * @Copyright
 * @Version 1.0
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparrowApplication.class)
public class CommissionServiceTest extends SparrowApplicationTest {
    @Autowired
    private CommissionSettleService commissionSettleService;

    @Test
    public void test() {
        // 分佣规则绑定
        commissionSettleService.orderScaleBindSettleRule(3000L,1220677568999591937L,
                CommissionBizType.PLAT_SERVICE,
                CommissionPackage.PLAT_SERVICE,
                1220389998554124289L);
    }

    @Test
    public void test2() {
        commissionSettleService.orderScaleBindSettleRule(18000L,
                1220677568999591940L,
                CommissionBizType.PHONE_DOWN,
                CommissionPackage.PHONE_DOWN,
                1220389998554124289L);
    }

    @Test
    public void test3() {
        commissionSettleService.waitSettleOrder(1220736198729928705L,
                CommissionBizType.PHONE_DOWN,
                CommissionPackage.PHONE_DOWN.getType(),
                1220389998554124289L,
                EmployAccountChangeEnum.mobile_complete_froze,
                EmployAccountChangeEnum.mobile_complete_froze.getRemark());
    }




}