package com.anyi.miniapp.product.controller;

import com.alibaba.fastjson2.JSONObject;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.enums.PayEnterEnum;
import com.anyi.common.product.domain.response.RefundPaymentInfoVO;
import com.anyi.common.service.PayCallbackRecordService;
import com.anyi.common.util.ValidatorUtil;
import com.anyi.common.wx.MchIdService;
import com.anyi.miniapp.product.service.RefundPaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "客户支付退款金额相关接口")
@RestController
@RequestMapping("/mobile/customer/refund/payment")
public class RefundPaymentController {
    @Autowired
    private RefundPaymentService service;
    @Autowired
    private PayCallbackRecordService payCallbackRecordService;
    @Autowired
    private MchIdService mchIdService;

    @ApiOperation(value = "根据订单号获取退款信息")
    @WebLog(description = "根据订单号获取退款信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public Response<RefundPaymentInfoVO> getOrderInfo(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.getRefundPaymentInfo(req.getId()));
    }

    @ApiOperation(value = "支付结果查询")
    @WebLog(description = "支付结果查询")
    @RequestMapping(value = "/pay/query", method = RequestMethod.POST)
    public Response<Boolean> payQuery(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.payQuery(req.getId()));
    }

    @ApiOperation(value = "手机订单退款支付回调")
    @WebLog(description = "手机订单退款支付回调")
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public Response<?> payNotify(@RequestBody @Valid @NotNull JSONObject json) {
        payCallbackRecordService.saveRecord(PayEnterEnum.CUSTOMER_REFUND_PAYMENT_NOTIFY.getUrl(), mchIdService.getJxzSubMchId(), json.toString());
        service.payNotify(json.toString());
        return Response.ok();
    }
}