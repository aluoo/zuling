package com.anyi.common.product.service;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.mapper.OrderCustomerReceivePaymentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/4
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderCustomerReceivePaymentService extends ServiceImpl<OrderCustomerReceivePaymentMapper, OrderCustomerReceivePayment> {

    public OrderCustomerReceivePayment getAvailableByOrderId(Long orderId) {
        if (orderId == null) {
            return null;
        }
        return this.lambdaQuery()
                .eq(OrderCustomerReceivePayment::getOrderId, orderId)
                .eq(OrderCustomerReceivePayment::getDeleted, false)
                .one();
    }

    public OrderCustomerReceivePayment getAvailableByOutBizNo(String outBizNo) {
        if (StrUtil.isBlank(outBizNo)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(OrderCustomerReceivePayment::getOutBizNo, outBizNo)
                .eq(OrderCustomerReceivePayment::getDeleted, false)
                .one();
    }
}