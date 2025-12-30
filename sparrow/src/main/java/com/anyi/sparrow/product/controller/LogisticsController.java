package com.anyi.sparrow.product.controller;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.request.CreateLogisticsReq;
import com.anyi.common.product.domain.request.PreCreateLogisticsReq;
import com.anyi.common.product.domain.request.ShippingOrderQueryReq;
import com.anyi.common.product.domain.response.PreCreateLogisticsVO;
import com.anyi.common.product.domain.response.ShippingOrderDetailVO;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.product.service.LogisticsManageService;
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
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "物流中心管理")
@RestController
@RequestMapping("/mobile/order/logistics/center")
public class LogisticsController {
    @Autowired
    private LogisticsManageService service;

    @ApiOperation(value = "1.物流下单发货前置-根据发货订单号获取物流下单信息")
    @WebLog(description = "物流下单发货前置-根据发货订单号获取物流下单信息")
    @RequestMapping(value = "/pre/create", method = RequestMethod.POST)
    public Response<PreCreateLogisticsVO> preCreateLogistics(@RequestBody PreCreateLogisticsReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.preCreateLogistics(req));
    }

    @RepeatSubmit
    @ApiOperation(value = "2.物流下单")
    @WebLog(description = "物流下单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response<?> createLogistics(@RequestBody CreateLogisticsReq req) {
        ValidatorUtil.validateBean(req);
        service.createLogistics(req);
        return Response.ok();
    }

    @ApiOperation(value = "3.物流发货订单列表")
    @WebLog(description = "物流发货订单列表")
    @RequestMapping(value = "/order/list", method = RequestMethod.POST)
    public Response<List<ShippingOrderDetailVO>> listShippingOrder(@RequestBody ShippingOrderQueryReq req) {
        return Response.ok(service.listShippingOrder(req));
    }

    @ApiOperation(value = "4.物流发货订单详情")
    @WebLog(description = "物流发货订单详情")
    @RequestMapping(value = "/order/detail", method = RequestMethod.POST)
    public Response<ShippingOrderDetailVO> detailShippingOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detailShippingOrder(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation(value = "5.取消发货")
    @WebLog(description = "取消发货")
    @RequestMapping(value = "/order/cancel", method = RequestMethod.POST)
    public Response<?> cancelShippingOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.cancelShippingOrder(req.getId());
        return Response.ok();
    }
}