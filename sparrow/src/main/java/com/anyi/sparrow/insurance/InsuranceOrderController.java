package com.anyi.sparrow.insurance;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.insurance.req.*;
import com.anyi.common.insurance.response.DiInsuranceInfoVO;
import com.anyi.common.insurance.response.InsuranceOrderApplyVO;
import com.anyi.common.insurance.response.InsuranceOrderDetailVO;
import com.anyi.common.insurance.response.InsuranceOrderRefundDetailVO;
import com.anyi.common.product.domain.request.OrderQueryReq;
import com.anyi.common.product.domain.request.RefundPaymentPayApplyReq;
import com.anyi.common.result.WxPayVO;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.insurance.service.InsuranceOrderManageService;
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
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "数保订单管理")
@RestController
@RequestMapping("/insurance/order")
public class InsuranceOrderController {
    @Autowired
    private InsuranceOrderManageService service;

    @ApiOperation("根据手机规格获取数保产品列表")
    @WebLog(description = "根据手机规格获取数保产品列表")
    @RequestMapping(value = "/list/di", method = RequestMethod.POST)
    public Response<List<DiInsuranceInfoVO>> listInsuranceByMobile(@RequestBody DiInsuranceInfoQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.listInsuranceByMobile(req));
    }

    @RepeatSubmit
    @ApiOperation("提交数保订单")
    @WebLog(description = "提交数保订单")
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public Response<InsuranceOrderApplyVO> applyOrder(@RequestBody InsuranceOrderApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.applyOrder(req));
    }

    @ApiOperation("数保订单列表")
    @WebLog(description = "数保订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<InsuranceOrderDetailVO>> listOrder(@RequestBody OrderQueryReq req) {
        return Response.ok(service.listOrder(req));
    }

    @ApiOperation("数保订单详情")
    @WebLog(description = "数保订单详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<InsuranceOrderDetailVO> detailOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detailOrder(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation("修改订单资料")
    @WebLog(description = "修改订单资料")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> editOrder(@RequestBody InsuranceOrderEditReq req) {
        ValidatorUtil.validateBean(req);
        service.editOrder(req);
        return Response.ok();
    }

    @RepeatSubmit
    @ApiOperation("取消订单")
    @WebLog(description = "取消订单")
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Response<?> cancelOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.cancelOrder(req.getId());
        return Response.ok();
    }

    @ApiOperation("获取申请退款详情信息")
    @WebLog(description = "获取申请退款详情信息")
    @RequestMapping(value = "/refund/apply/info", method = RequestMethod.POST)
    public Response<InsuranceOrderRefundDetailVO> getRefundInfo(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.getRefundInfo(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation("申请退款")
    @WebLog(description = "申请退款")
    @RequestMapping(value = "/refund/apply", method = RequestMethod.POST)
    public Response<?> applyRefund(@RequestBody InsuranceOrderRefundApplyReq req) {
        ValidatorUtil.validateBean(req);
        service.applyRefund(req);
        return Response.ok();
    }

    @ApiOperation("获取微信支付二维码信息")
    @WebLog(description = "获取微信支付二维码信息")
    @RequestMapping(value = "/pay/wechat/info", method = RequestMethod.POST)
    public Response<InsuranceOrderPaymentInfoVO> applyWechatPay(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.applyWechatPay(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation("数保订单微信支付")
    @WebLog(description = "数保订单微信支付")
    @RequestMapping(value = "/pay/wechat", method = RequestMethod.POST)
    public Response<WxPayVO> wechatPay(@RequestBody RefundPaymentPayApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.pay(req.getId(), req.getAuthCode()));
    }

    @RepeatSubmit
    @ApiOperation("数保订单账户余额支付")
    @WebLog(description = "数保订单账户余额支付")
    @RequestMapping(value = "/pay/balance", method = RequestMethod.POST)
    public Response<?> applyBalancePay(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.applyBalancePay(req.getId());
        return Response.ok();
    }
}