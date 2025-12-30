package com.anyi.commission.service;

import cn.hutool.json.JSONUtil;
import com.anyi.common.service.ProductWxPayService;
import com.anyi.common.wx.MchIdService;
import com.anyi.sparrow.SparrowApplicationTest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsQueryRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsRequest;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsResult;
import com.github.binarywang.wxpay.bean.ecommerce.TransactionsResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/16
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class WechatPayTest extends SparrowApplicationTest {
    @Autowired
    ProductWxPayService productWxPayService;
    @Autowired
    MchIdService mchIdService;
    @Autowired
    WxPayService wxPayService;

    @Test
    // @Ignore
    public void payApply() {
        String openId = "o1d7G6zKmeAM3_nKbgnDdAI5iQLs";
        String otn = "jxz-change-test-01";
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsRequest.Amount amount = new PartnerTransactionsRequest.Amount();
        amount.setCurrency(WxPayConstants.CurrencyType.CNY);
        amount.setTotal(1);
        PartnerTransactionsRequest.Payer payer = new PartnerTransactionsRequest.Payer();
        payer.setSubOpenid(openId);
        PartnerTransactionsRequest req = PartnerTransactionsRequest.builder()
                .spAppid(wxPayService.getConfig().getAppId())
                .spMchid(wxPayService.getConfig().getMchId())
                .subAppid(wxPayService.getConfig().getSubAppId())
                .subMchid(wxPayService.getConfig().getSubMchId())
                .notifyUrl("https://quote-test.anyichuxing.com/test/notify")
                .description("购机款退还")
                .outTradeNo(otn)
                .amount(amount)
                .payer(payer)
                .build();
        TransactionsResult.JsapiResult res = productWxPayService.partnerTransactions(req);
        log.info(JSONUtil.toJsonStr(res));
    }

    @Test
    public void payQuery() {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        PartnerTransactionsQueryRequest queryReq = new PartnerTransactionsQueryRequest();
        // queryReq.setOutTradeNo(CommonWxUtils.createUnionTradeNo());
        queryReq.setOutTradeNo("jxz-change-test-01");
        queryReq.setSpMchid(wxPayService.getConfig().getMchId());
        queryReq.setSubMchid(wxPayService.getConfig().getSubMchId());
        PartnerTransactionsResult res = productWxPayService.queryPartnerTransactions(queryReq);
        log.debug(JSONUtil.toJsonStr(res));
    }

    @Test
    // @Ignore
    public void refundV3() throws WxPayException {
        wxPayService.switchover(mchIdService.getJxzSubMchId());
        WxPayRefundV3Request req = new WxPayRefundV3Request();
        req.setSubMchid(wxPayService.getConfig().getSubMchId());
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
        amount.setTotal(10);
        amount.setRefund(10);
        amount.setCurrency("CNY");
        req.setAmount(amount);
        req.setOutTradeNo("AY2024031615472615776311643688");
        req.setOutRefundNo("RF2024031615472615776311643688");
        wxPayService.refundV3(req);
    }

    @Test
    public void closeOrder() {
        productWxPayService.closePartnerTransactions("jxz-change-test-01");
    }
}