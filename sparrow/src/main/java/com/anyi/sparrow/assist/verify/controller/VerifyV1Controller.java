package com.anyi.sparrow.assist.verify.controller;

import com.anyi.sparrow.assist.verify.enums.BizEnum;
import com.anyi.sparrow.assist.verify.service.VerifyService;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "短信验证码接口-新版本")
public class VerifyV1Controller {
    @Autowired
    private VerifyService verifyService;

    @ApiOperation("发送验证码")
    @PostMapping("eapp/sms/v1.0/code/send")
    public Response sendCode(@RequestParam String mobile, @RequestParam BizEnum biz){
        verifyService.sendVerify(mobile, biz);
        return Response.ok();
    }
    @ApiOperation("检查验证码是否正确")
    @PostMapping("eapp/sms/v1.0/code/check")
    public Response verifyCode(@RequestParam String mobile, @RequestParam BizEnum biz, @RequestParam String code){
        verifyService.verifyCode(mobile, code, biz);
        return Response.ok();
    }
}
