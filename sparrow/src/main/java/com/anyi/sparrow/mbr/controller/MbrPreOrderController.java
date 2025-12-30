package com.anyi.sparrow.mbr.controller;

import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.mbr.req.MbrPreOrderApplyReq;
import com.anyi.common.mbr.req.PreOrderQueryReq;
import com.anyi.common.mbr.response.MbrOrderDetailVO;
import com.anyi.common.mbr.response.MbrPlatQuoteLogVO;
import com.anyi.common.mbr.response.MbrPreOrderApplyVO;
import com.anyi.common.mbr.response.MbrPreOrderDetailVO;
import com.anyi.common.result.DictMapVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.mbr.service.MbrOrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "租机进件单管理")
@RestController
@RequestMapping("/mbr/pre/order")
public class MbrPreOrderController {

    @Autowired
    CommonSysDictService commonSysDictService;
    @Autowired
    MbrOrderManageService mbrOrderManageService;

    @ApiOperation("租借期数列表")
    @WebLog(description = "租借期数列表")
    @GetMapping("/period")
    public Response<DictMapVO> periodItem() {
        return Response.ok(commonSysDictService.getNameMap("mbr_order_period"));
    }

    @RepeatSubmit
    @ApiOperation("提交租机进件单")
    @WebLog(description = "提交租机进件单")
    @PostMapping(value = "/apply")
    public Response<MbrPreOrderApplyVO> applyOrder(@RequestBody MbrPreOrderApplyReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(mbrOrderManageService.applyOrder(req));
    }

    @ApiOperation("租机进件单详情")
    @WebLog(description = "租机进件单详情")
    @PostMapping(value = "/detail")
    public Response<MbrPreOrderDetailVO> detailOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(mbrOrderManageService.preOrderDetail(req));
    }

    @ApiOperation("根据订单号刷新租机商审核状态")
    @WebLog(description = "根据订单号刷新租机商审核状态")
    @PostMapping(value = "/list/quote/info")
    public Response<List<MbrPlatQuoteLogVO>> listQuoteInfoByOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(mbrOrderManageService.listQuoteInfoByOrder(req));
    }

    @ApiOperation("继续租机功能")
    @WebLog(description = "继续租机功能")
    @PostMapping(value = "/rent")
    public Response<MbrOrderDetailVO> rent(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(mbrOrderManageService.rent(req));
    }

    @ApiOperation("租机进件单列表")
    @WebLog(description = "租机进件单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<MbrPreOrderDetailVO>> listOrder(@RequestBody PreOrderQueryReq req) {
        return Response.ok(mbrOrderManageService.preListOrder(req));
    }
}