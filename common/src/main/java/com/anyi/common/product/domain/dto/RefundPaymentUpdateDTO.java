package com.anyi.common.product.domain.dto;

import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsNotifyResult;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundPaymentUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String outTradeNo;
    private String transactionId;
    private String tradeState;
    private String tradeStateDesc;
    private String successTime;
    private String openId;

    public RefundPaymentUpdateDTO(PartnerTransactionsNotifyResult notifyResult) {
        this.outTradeNo = notifyResult.getResult().getOutTradeNo();
        this.transactionId = notifyResult.getResult().getTransactionId();
        this.tradeState = notifyResult.getResult().getTradeState();
        this.tradeStateDesc = notifyResult.getResult().getTradeStateDesc();
        this.successTime = notifyResult.getResult().getSuccessTime();
        this.openId = notifyResult.getResult().getPayer().getSubOpenid();
    }

    public RefundPaymentUpdateDTO(PartnerTransactionsResult res) {
        this.outTradeNo = res.getOutTradeNo();
        this.transactionId = res.getTransactionId();
        this.tradeState = res.getTradeState();
        this.tradeStateDesc = res.getTradeStateDesc();
        this.successTime = res.getSuccessTime();
        this.openId = res.getCombinePayerInfo().getSubOpenid();
    }
}