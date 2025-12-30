package com.anyi.sparrow.alipay.controller;

import cn.hutool.json.JSONUtil;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.product.domain.request.AlipayReceivePaymentTransferReq;
import com.anyi.sparrow.alipay.service.AliPayService;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class AlipayController {
    private Logger logger = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    private AliPayService aliPayService;

    @ApiOperation("支付宝openId")
    @ResponseBody
    @RequestMapping(value = "/alipay/getOpenId", method = RequestMethod.GET)
    public Response<AlipaySystemOauthTokenResponse> getOpenId() {
        return Response.ok(aliPayService.getOpenId("21321321312"));
    }

    @ApiOperation("支付宝转账申请")
    @WebLog(description = "支付宝转账申请")
    @RequestMapping(value = "/alipay/receive/payment/transfer", method = RequestMethod.POST)
    public Response<?> receivePaymentTransfer(@RequestBody AlipayReceivePaymentTransferReq req) {
        ValidatorUtil.validateBean(req);
        aliPayService.receivePaymentTransfer(req);
        return Response.ok();
    }

    @WebLog(description = "支付宝转账回调")
    @RequestMapping(value = "/alipayNotify", method = RequestMethod.POST)
    public Response<?> alipayNotify(HttpServletRequest request) {
        // todo
        log.info("receive alipay notify: {}", JSONUtil.toJsonStr(request));
        Map<String, String[]> allParam = request.getParameterMap();
        log.info("receive alipay notify parameter map: {}", JSONUtil.toJsonStr(allParam));
        return Response.ok();
    }
}