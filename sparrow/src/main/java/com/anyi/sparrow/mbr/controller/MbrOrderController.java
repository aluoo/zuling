package com.anyi.sparrow.mbr.controller;

import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.param.IdQueryReq;
import com.anyi.common.domain.param.Response;
import com.anyi.common.mbr.req.PreOrderQueryReq;
import com.anyi.common.mbr.req.ShopSuppleReq;
import com.anyi.common.mbr.response.MbrOrderDetailVO;
import com.anyi.common.mbr.response.MbrShopCodeVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.sparrow.base.security.LoginUserContext;
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
@Api(tags = "租机单管理")
@RestController
@RequestMapping("/mbr/order")
public class MbrOrderController {

    @Autowired
    CommonSysDictService commonSysDictService;
    @Autowired
    MbrOrderManageService mbrOrderManageService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    @ApiOperation("租机订单列表")
    @WebLog(description = "租机订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<List<MbrOrderDetailVO>> listOrder(@RequestBody PreOrderQueryReq req) {
        return Response.ok(mbrOrderManageService.listOrder(req));
    }


    @ApiOperation("租机订单详情")
    @WebLog(description = "租机订单详情")
    @PostMapping(value = "/detail")
    public Response<MbrOrderDetailVO> detailOrder(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(mbrOrderManageService.rentDetail(req.getId()));
    }

    @ApiOperation("店铺或者业务员推广二维码")
    @WebLog(description = "店铺或者业务员推广二维码")
    @PostMapping(value = "/shopCode")
    public Response<MbrShopCodeVO> shopCode() {
        String lockKey = StrUtil.format(RedisLockKeyConstants.MBR_SHOP_CODE_LOCK_KEY, LoginUserContext.getUser().getId());
        MbrShopCodeVO mbrShopCodeVO = distributionLockUtil.lock(
                () -> mbrOrderManageService.shopCode(),
                0,
                () -> lockKey,
                BizError.MBR_SHOP_CODE.getMessage(),
                null);
        return Response.ok(mbrShopCodeVO);
    }

    @ApiOperation("门店信息补填")
    @WebLog(description = "门店信息补填")
    @RequestMapping(value = "/supple", method = RequestMethod.POST)
    public Response supple(@RequestBody ShopSuppleReq req) {
        mbrOrderManageService.supple(req);
        return Response.ok();
    }

    @ApiOperation("获取门店结算地址")
    @WebLog(description = "获取门店结算地址")
    @RequestMapping(value = "/settleLink", method = RequestMethod.POST)
    public Response settleLink() {
        return Response.ok(mbrOrderManageService.settleLink());
    }

}