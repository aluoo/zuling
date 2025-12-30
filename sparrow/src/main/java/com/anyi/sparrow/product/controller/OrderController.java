package com.anyi.sparrow.product.controller;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.dto.OrderQuoteInfoDTO;
import com.anyi.common.product.domain.request.*;
import com.anyi.common.product.domain.response.OrderApplyVO;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.domain.response.ReceivePaymentInfoVO;
import com.anyi.common.product.domain.response.RefundPaymentInfoVO;
import com.anyi.common.result.WxPayVO;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.product.service.OrderManageService;
import com.anyi.sparrow.product.service.RefundManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/1
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "报价订单管理")
@RestController
@RequestMapping("/mobile/order")
public class OrderController {
    @Autowired
    private OrderManageService service;
    @Autowired
    private RefundManageService refundManageService;

    @ApiOperation("订单列表")
    @WebLog(description = "订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<OrderDetailVO>> listOrder(@RequestBody OrderQueryReq req) {
        return Response.ok(service.listOrder(req));
    }

    @RepeatSubmit
    @ApiOperation("提交订单")
    @WebLog(description = "提交订单")
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public Response<OrderApplyVO> applyOrder(@RequestBody OrderApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.applyOrder(req));
    }

    @RepeatSubmit
    @ApiOperation("确认交易")
    @WebLog(description = "确认交易")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public Response<OrderApplyVO> confirmOrderQuote(@RequestBody OrderConfirmQuoteReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.confirmOrderQuote(req));
    }

    @RepeatSubmit
    @ApiOperation("取消订单")
    @WebLog(description = "取消订单")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Response<?> cancelOrder(@RequestBody OrderCancelReq req) {
        ValidatorUtil.validateBean(req);
        service.cancelOrder(req);
        return Response.ok();
    }

    @ApiOperation("订单详情")
    @WebLog(description = "订单详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<OrderDetailVO> detailOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detailOrder(req.getId()));
    }

    @ApiOperation("根据订单号获取报价列表(刷新报价)")
    @WebLog(description = "根据订单号获取报价列表(刷新报价)")
    @RequestMapping(value = "/list/quote/info", method = RequestMethod.POST)
    public Response<List<OrderQuoteInfoDTO>> listQuoteInfoByOrder(@RequestBody OrderQuoteQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.listQuoteInfoByOrder(req));
    }

    @ApiOperation("根据订单号获取收款信息")
    @WebLog(description = "根据订单号获取收款信息")
    @RequestMapping(value = "/receive/payment/info", method = RequestMethod.POST)
    public Response<ReceivePaymentInfoVO> getReceivePaymentInfo(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.getReceivePaymentInfo(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation("提交收款信息并返回二维码")
    @WebLog(description = "提交收款信息并返回二维码")
    @RequestMapping(value = "/receive/payment/apply", method = RequestMethod.POST)
    public Response<ReceivePaymentInfoVO> applyReceivePayment(@RequestBody ReceivePaymentApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.applyReceivePayment(req));
    }

    @ApiOperation(value = "根据订单号获取退款信息", notes = "退款信息为空的时候需要提交申请，否则根据状态显示已支付（退款）或未支付（退款）")
    @WebLog(description = "根据订单号获取退款信息")
    @RequestMapping(value = "/refund/payment/info", method = RequestMethod.POST)
    public Response<RefundPaymentInfoVO> getRefundPaymentInfo(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(refundManageService.getRefundPaymentInfo(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation(value = "提交退款信息并返回二维码")
    @WebLog(description = "提交退款信息并返回二维码")
    @RequestMapping(value = "/refund/payment/apply", method = RequestMethod.POST)
    public Response<RefundPaymentInfoVO> applyRefundPaymentInfo(@RequestBody RefundPaymentApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(refundManageService.apply(req));
    }

    @RepeatSubmit
    @ApiOperation(value = "申请订单退款支付")
    @WebLog(description = "申请订单退款支付")
    @RequestMapping(value = "/refund/payment/pay", method = RequestMethod.POST)
    public Response<WxPayVO> pay(@RequestBody RefundPaymentPayApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(refundManageService.pay(req.getId(), req.getAuthCode()));
    }
}