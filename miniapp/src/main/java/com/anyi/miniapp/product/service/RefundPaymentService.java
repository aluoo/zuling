package com.anyi.miniapp.product.service;

import com.anyi.common.advice.BaseException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.domain.OrderCustomerRefundPayment;
import com.anyi.common.product.domain.dto.RefundPaymentUpdateDTO;
import com.anyi.common.product.domain.enums.RefundPaymentStatusEnum;
import com.anyi.common.product.domain.response.ReceivePaymentInfoVO;
import com.anyi.common.service.AbstractRefundPaymentManage;
import com.anyi.common.service.ProductWxPayService;
import com.anyi.common.wx.MchIdService;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsNotifyResult;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class RefundPaymentService extends AbstractRefundPaymentManage {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ProductWxPayService productWxPayService;
    @Autowired
    private MchIdService mchIdService;

    @Transactional(rollbackFor = Exception.class)
    public boolean payQuery(Long orderId) {
        OrderCustomerRefundPayment payment = super.getRefundPayment(orderId);
        if (payment.getStatus().equals(RefundPaymentStatusEnum.PAYED.getCode())) {
            log.info("payQuery.info: 订单已支付");
            return true;
        }

        // query
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsQueryRequest queryReq = super.buildPayQueryReq(payment.getOutTradeNo(), wxPayService);
        PartnerTransactionsResult res;
        try {
            res = productWxPayService.queryPartnerTransactions(queryReq);
        } catch (BaseException e) {
            return false;
        }
        if (res == null) {
            log.info("payOrderQuery.info: queryPartnerTransactions-res is null");
            return false;
        }
        boolean payed = WxPayConstants.WxpayTradeStatus.SUCCESS.equals(res.getTradeState());
        if (!payed) {
            log.info("payOrderQuery.info: 微信订单查询状态为-{}", res.getTradeStateDesc());
            return false;
        }
        log.info("payOrderQuery.info: 微信订单查询状态为-{}-{}", res.getTradeState(), res.getTradeStateDesc());

        // pay success update order
        RefundPaymentUpdateDTO dto = new RefundPaymentUpdateDTO(res);
        super.paySuccess(dto);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public void payNotify(String notify) {
        PartnerTransactionsNotifyResult notifyResult = productWxPayService.parseNotify(notify, mchIdService.getJxzSubMchId());
        RefundPaymentUpdateDTO dto = new RefundPaymentUpdateDTO(notifyResult);
        super.paySuccess(dto);
    }

    @Override
    protected Map<Long, Employee> getEmployeeInfoMap(ReceivePaymentInfoVO vo) {
        return null;
    }

    @Override
    protected Map<Long, Company> getCompanyInfoMap(ReceivePaymentInfoVO vo) {
        return null;
    }

    @Override
    protected void setReceivePaymentService(ReceivePaymentInfoVO vo, OrderCustomerReceivePayment payment) {
    }
}