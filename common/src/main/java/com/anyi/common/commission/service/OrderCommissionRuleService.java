package com.anyi.common.commission.service;

import com.anyi.common.commission.domain.CommissionSettle;
import com.anyi.common.commission.domain.OrderCommissionRule;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.mapper.CommissionSettleMapper;
import com.anyi.common.commission.mapper.OrderCommissionRuleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class OrderCommissionRuleService extends ServiceImpl<OrderCommissionRuleMapper, OrderCommissionRule> {


    public Boolean checkOrderRuleExists(Long orderId, CommissionBizType typeEnum,Long commissionPackageId) {
        Long count = this.lambdaQuery()
                .eq(OrderCommissionRule::getOrderId, orderId)
                .eq(OrderCommissionRule::getCommissionType, typeEnum.getType())
                .eq(OrderCommissionRule::getCommissionPackage, commissionPackageId)
                .count();
        return (count == null || count == 0) ? false : true;
    }


    public OrderCommissionRule getRuleVersionByOrderId(Long orderId,CommissionBizType typeEnum,Long commissionPackageId) {
        return this.lambdaQuery().eq(OrderCommissionRule::getOrderId, orderId)
                .eq(OrderCommissionRule::getCommissionType, typeEnum.getType())
                .eq(OrderCommissionRule::getCommissionPackage, commissionPackageId)
                .one();
    }


}