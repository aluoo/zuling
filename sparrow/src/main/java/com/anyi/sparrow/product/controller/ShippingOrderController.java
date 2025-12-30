package com.anyi.sparrow.product.controller;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.request.RecyclerListQueryReq;
import com.anyi.common.product.domain.request.RecyclerOrderQueryReq;
import com.anyi.common.product.domain.request.RecyclerOrderVerifyCountQueryReq;
import com.anyi.common.product.domain.request.ShippingOrderCreateReq;
import com.anyi.common.product.domain.response.OrderApplyVO;
import com.anyi.common.product.domain.response.RecyclerOrderVerifyCountVO;
import com.anyi.common.product.domain.response.ShippingRecyclerInfo;
import com.anyi.common.product.domain.response.ShippingRecyclerOrderInfoVO;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.product.service.ShippingOrderManageService;
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
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "发货中心管理")
@RestController
@RequestMapping("/mobile/order/shipping/center")
public class ShippingOrderController {
    @Autowired
    private ShippingOrderManageService service;

    @ApiOperation("发货中心首页-回收商列表")
    @WebLog(description = "发货中心首页-回收商列表")
    @RequestMapping(value = "/recycler/list", method = RequestMethod.POST)
    public Response<List<ShippingRecyclerInfo>> recyclerList(@RequestBody RecyclerListQueryReq req) {
        return Response.ok(service.recyclerList());
    }

    @ApiOperation(value = "回收商状态校验")
    @WebLog(description = "回收商状态校验")
    @RequestMapping(value = "/recycler/validate", method = RequestMethod.POST)
    public Response<?> validateRecycler(@RequestBody RecyclerListQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.validateRecycler(req.getCompanyId());
        return Response.ok();
    }

    @ApiOperation("回收商订单列表")
    @WebLog(description = "回收商订单列表")
    @RequestMapping(value = "/recycler/order/list", method = RequestMethod.POST)
    public Response<List<ShippingRecyclerOrderInfoVO>> recyclerOrderList(@RequestBody RecyclerOrderQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.recyclerOrderList(req));
    }

    @ApiOperation("回收商订单列表-获取已核验/未核验数量")
    @WebLog(description = "回收商订单列表-获取已核验/未核验数量")
    @RequestMapping(value = "/recycler/order/list/count", method = RequestMethod.POST)
    public Response<RecyclerOrderVerifyCountVO> recyclerOrderVerifyCount(@RequestBody RecyclerOrderVerifyCountQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.recyclerOrderVerifyCount(req.getCompanyId()));
    }

    @RepeatSubmit
    @ApiOperation(value = "订单码绑定")
    @WebLog(description = "订单码绑定")
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public Response<?> bindOrderNo(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.bindOrderNo(req.getId());
        return Response.ok();
    }

    @RepeatSubmit
    @ApiOperation(value = "订单核验")
    @WebLog(description = "订单核验")
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public Response<?> verifyOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.verifyOrder(req.getId());
        return Response.ok();
    }

    @RepeatSubmit
    @ApiOperation(value = "核验后立即下单-创建发货订单")
    @WebLog(description = "核验后立即下单-创建发货订单")
    @RequestMapping(value = "/create/order", method = RequestMethod.POST)
    public Response<OrderApplyVO> createShippingOrder(@RequestBody ShippingOrderCreateReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.createShippingOrder(req));
    }
}