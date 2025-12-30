package com.anyi.sparrow.hk.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.hk.domain.HkApplyOrder;
import com.anyi.common.hk.service.HkApplyOrderService;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.sparrow.hk.dto.HkNotifyReq;
import com.anyi.sparrow.hk.service.HkNotifyProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenjian
 * @Description
 * @Date 2025/6/3
 * @Copyright
 * @Version 1.0
 */
@Api(tags = "号卡回调管理")
@Controller
@Slf4j
@RequestMapping("/hk/order/notify")
public class HkNotifyController {

    @Autowired
    private HkNotifyProcessService hkNotifyProcessService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;
    @Autowired
    private HkApplyOrderService hkApplyOrderService;

    @ApiOperation("号卡回调接口")
    @WebLog(description = "号卡回调接口")
    @ResponseBody
    @RequestMapping(value = "/log", method = RequestMethod.POST,produces = MediaType.TEXT_PLAIN_VALUE)
    public void notifyOrder(@RequestBody HkNotifyReq req, HttpServletResponse response) throws IOException {
        log.info("号卡回调接口:{}",req.getOrder_sn());
        //防止回调过快休眠4秒
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String lockKey = StrUtil.format("hk_notify_lock-{}", req.getThird_order_sn());
        distributionLockUtil.lock(
                () -> hkNotifyProcessService.doneOrder(req),
                2000,
                () -> lockKey,
                "处理中",
                null);
        response.setContentType("text/plain");
        response.getWriter().write("SUCCESS");
    }





}