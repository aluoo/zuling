package com.anyi.sparrow.mbr.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.mbr.req.MbrHwzCreateOrderNotifyReq;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.sparrow.mbr.service.MbrNotifyProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjian
 * @Description
 * @Date 2025/6/3
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "租机单回调管理")
@RestController
@Slf4j
@RequestMapping("/mbr/order/notify")
public class MbrOrderNotifyController {

    @Autowired
    MbrNotifyProcessService mbrNotifyProcessService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    @ApiOperation("订单创建回调接口")
    @WebLog(description = "订单创建回调接口")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public Map<String, Object> createOrder(@RequestBody MbrHwzCreateOrderNotifyReq req) {
        log.info("荟玩租订单创建回调接口:{}", JSONUtil.toJsonStr(req));
        String lockKey = StrUtil.format("order_notify_lock-{}", req.getData().getSaleOrderId());
        distributionLockUtil.lock(
                () -> mbrNotifyProcessService.doneOrder(req),
                2000,
                () -> lockKey,
                "处理中",
                null);
        Map<String, Object> result = new HashMap<>();
        result.put("code", "000000" );
        result.put("message",  "success" );
        return result;
    }





}