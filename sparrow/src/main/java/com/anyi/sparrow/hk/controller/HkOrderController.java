package com.anyi.sparrow.hk.controller;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.hk.domain.HkOperator;
import com.anyi.common.hk.service.HkOperateService;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.hk.dto.*;
import com.anyi.sparrow.hk.enums.HkOrderStatusEnum;
import com.anyi.sparrow.hk.service.HkOrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjian
 * @Description
 * @Date 2025/7/31
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "号卡管理")
@RestController
@RequestMapping("/hk/order")
public class HkOrderController {

    @Autowired
    HkOrderManageService hkOrderManageService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;
    @Autowired
    private HkOperateService hkOperateService;

    @ApiOperation("号卡订单列表")
    @WebLog(description = "号卡订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<HkOrderDetailVO>> listOrder(@RequestBody HkOrderQueryReq req) {
        return Response.ok(hkOrderManageService.listOrder(req));
    }

    @ApiOperation("状态列表")
    @WebLog(description = "状态列表")
    @GetMapping("/status/list")
    public Response<List<Map>> statusList() {
        List<Map> resultList = new ArrayList<>();
        for (HkOrderStatusEnum typeEnum : HkOrderStatusEnum.values()) {
            Map<Integer, String> resultMap = new HashMap();
            resultMap.put(typeEnum.getCode(),typeEnum.getName());
            resultList.add(resultMap);
        }
        return Response.ok(resultList);
    }

    @ApiOperation("号卡订单详情")
    @WebLog(description = "号卡订单详情")
    @PostMapping(value = "/detail")
    public Response<HkOrderDetailVO> detailOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(hkOrderManageService.detail(req.getId()));
    }

    @ApiOperation("号卡办理")
    @WebLog(description = "号卡办理")
    @PostMapping(value = "/apply")
    public Response apply(@RequestBody ApplyOrderReq req) {
        String lockKey = StrUtil.format("hk_apply_lock-{}", LoginUserContext.getUser().getId());
        distributionLockUtil.lock(
                () -> hkOrderManageService.applyOrder(req),
                2000,
                () -> lockKey,
                "处理中",
                null);
        return Response.ok();
    }

    @ApiOperation("产品运营商列表")
    @WebLog(description = "产品运营商列表")
    @RequestMapping(value = "/operateList", method = RequestMethod.POST)
    public Response<List<HkOperator>> operateList() {
        return Response.ok(hkOperateService.lambdaQuery().eq(HkOperator::getStatus,1).list());
    }

    @ApiOperation("号卡产品列表")
    @WebLog(description = "号卡产品列表")
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public Response<List<HkProductDetailVO>> productList(@RequestBody ProductQueryReq req) {
        return Response.ok(hkOrderManageService.productList(req));
    }

    @ApiOperation("选号列表")
    @WebLog(description = "选号列表")
    @RequestMapping(value = "/taskNumber", method = RequestMethod.POST)
    public Response<List<TaskNumberRsp>> taskNumber(@RequestBody TaskNumberReq req){
        return Response.ok(hkOrderManageService.taskNumber(req));
    }

}