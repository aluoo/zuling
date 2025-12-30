package com.anyi.miniapp.product.controller;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.request.RecyclerCancelQuoteReq;
import com.anyi.common.product.domain.request.RecyclerQuoteLogListReq;
import com.anyi.common.product.domain.request.RecyclerQuoteReq;
import com.anyi.common.product.domain.request.RecyclerRejectQuoteReq;
import com.anyi.common.product.domain.response.RecyclerLockQuoteVO;
import com.anyi.common.product.domain.response.RecyclerQuoteCountInfoVO;
import com.anyi.common.product.domain.response.RecyclerQuoteLogInfoVO;
import com.anyi.common.product.domain.response.RecyclerQuotePriceInfoVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.util.ValidatorUtil;
import com.anyi.miniapp.product.service.QuoteCenterService;
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
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "报价中心管理")
@RestController
@RequestMapping("/mobile/recycler/quote/center")
public class QuoteCenterController {
    @Autowired
    private QuoteCenterService service;
    @Autowired
    private CommonSysDictService sysDictService;

    @ApiOperation("1.报价单数量统计")
    @WebLog(description = "报价单数量统计")
    @RequestMapping(value = "/list/count", method = RequestMethod.POST)
    public Response<RecyclerQuoteCountInfoVO> countQuoteLog(@RequestBody RecyclerQuoteLogListReq req) {
        return Response.ok(service.countQuoteLog(req));
    }

    @ApiOperation("2.报价单列表")
    @WebLog(description = "报价单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<RecyclerQuoteLogInfoVO>> listQuoteLog(@RequestBody RecyclerQuoteLogListReq req) {
        return Response.ok(service.listOrder(req));
    }

    @RepeatSubmit
    @ApiOperation("3.抢单")
    @WebLog(description = "抢单")
    @RequestMapping(value = "/lock/quote", method = RequestMethod.POST)
    public Response<RecyclerLockQuoteVO> lockQuote(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.lockQuote(req.getId()));
    }

    @ApiOperation("4.根据报价金额计算平台服务费")
    @WebLog(description = "根据报价金额计算平台服务费")
    @RequestMapping(value = "/calc/quote", method = RequestMethod.POST)
    public Response<RecyclerQuotePriceInfoVO> calcQuotePrice(@RequestBody RecyclerQuoteReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.calcQuotePriceInfo(req));
    }

    @RepeatSubmit
    @ApiOperation("5.提交报价")
    @WebLog(description = "提交报价")
    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    public Response<?> quote(@RequestBody RecyclerQuoteReq req) {
        ValidatorUtil.validateBean(req);
        service.quote(req);
        return Response.ok();
    }

    @RepeatSubmit
    @ApiOperation("6.取消报价（跳过）")
    @WebLog(description = "取消报价")
    @RequestMapping(value = "/cancel/quote", method = RequestMethod.POST)
    public Response<?> cancelQuote(@RequestBody RecyclerCancelQuoteReq req) {
        ValidatorUtil.validateBean(req);
        service.cancelQuote(req);
        return Response.ok();
    }

    @RepeatSubmit
    @ApiOperation("7.拒绝报价")
    @WebLog(description = "拒绝报价")
    @RequestMapping(value = "/reject/quote", method = RequestMethod.POST)
    public Response<?> rejectQuote(@RequestBody RecyclerRejectQuoteReq req) {
        ValidatorUtil.validateBean(req);
        service.rejectQuote(req);
        return Response.ok();
    }

    @ApiOperation("8.获取拒绝报价原因列表")
    @WebLog(description = "获取拒绝报价原因列表")
    @RequestMapping(value = "/reject/quote/reason/list", method = RequestMethod.POST)
    public Response<List<String>> getRejectReasons() {
        return Response.ok(sysDictService.getRejectQuteReason());
    }
}