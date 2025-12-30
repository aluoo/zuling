package com.anyi.miniapp.product.controller;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.request.ShippingOrderQueryReq;
import com.anyi.common.product.domain.response.OrderDetailVO;
import com.anyi.common.product.domain.response.RecyclerShippingOrderCountInfoVO;
import com.anyi.common.product.domain.response.ShippingOrderDetailVO;
import com.anyi.common.util.ValidatorUtil;
import com.anyi.miniapp.product.service.OrderCenterService;
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
 * @Date 2024/3/12
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "回收商订单中心管理")
@RestController
@RequestMapping("/mobile/recycler/order/center")
public class OrderCenterController {
    @Autowired
    private OrderCenterService service;

    @ApiOperation(value = "1.订单详情", notes = "根据报价订单ID获取报价订单详情")
    @WebLog(description = "回收商订单中心-订单详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<OrderDetailVO> detailOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detailOrder(req.getId()));
    }

    @ApiOperation(value = "2.收货订单数量统计", notes = "获取待收货订单的数量统计")
    @WebLog(description = "回收商订单中心-收货订单数量统计")
    @RequestMapping(value = "/shipping/list/count", method = RequestMethod.POST)
    public Response<RecyclerShippingOrderCountInfoVO> countShippingOrder() {
        return Response.ok(service.countShippingOrder());
    }

    @ApiOperation(value = "3.收货订单列表")
    @WebLog(description = "回收商订单中心-收货订单列表")
    @RequestMapping(value = "/shipping/list", method = RequestMethod.POST)
    public Response<List<ShippingOrderDetailVO>> listShippingOrder(@RequestBody ShippingOrderQueryReq req) {
        return Response.ok(service.listShippingOrder(req));
    }

    @ApiOperation(value = "4.收货订单详情")
    @WebLog(description = "回收商订单中心-收货订单详情")
    @RequestMapping(value = "/shipping/detail", method = RequestMethod.POST)
    public Response<ShippingOrderDetailVO> detailShippingOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detailShippingOrder(req.getId()));
    }

    @RepeatSubmit
    @ApiOperation(value = "5.确认收货")
    @WebLog(description = "回收商订单中心-确认收货")
    @RequestMapping(value = "/shipping/confirm/receipt", method = RequestMethod.POST)
    public Response<?> confirmReceipt(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.confirmReceipt(req.getId());
        return Response.ok();
    }
}