package com.anyi.common.product.service;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.OrderCustomerRefundPayment;
import com.anyi.common.product.mapper.OrderCustomerRefundPaymentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderCustomerRefundPaymentService extends ServiceImpl<OrderCustomerRefundPaymentMapper, OrderCustomerRefundPayment> {

    public OrderCustomerRefundPayment getAvailableByOrderId(Long orderId) {
        if (orderId == null) {
            return null;
        }
        return this.lambdaQuery()
                .eq(OrderCustomerRefundPayment::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
    }
}