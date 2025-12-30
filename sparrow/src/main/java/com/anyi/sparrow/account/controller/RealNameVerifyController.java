package com.anyi.sparrow.account.controller;

import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.account.dto.response.EmployeeRealNameVerificationVO;
import com.anyi.sparrow.account.req.EmployeeRealNameVerificationReq;
import com.anyi.sparrow.account.service.impl.RealNameVerifyService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Slf4j
@Api(tags = "办理员实名认证模块相关接口")
@RestController
@RequestMapping("/eapp/v1.0/account/verify")
public class RealNameVerifyController {
    @Autowired
    private RealNameVerifyService service;

    @ApiOperation("获取员工实名认证信息")
    @PostMapping("/info")
    public Response<EmployeeRealNameVerificationVO> getInfo() {
        return Response.ok(service.getInfoByEmployeeId(LoginUserContext.getUser().getId()));
    }

    @ApiOperation("保存/修改员工实名认证信息")
    @PostMapping("/save")
    public Response<EmployeeRealNameVerificationVO> save(@RequestBody EmployeeRealNameVerificationReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.saveTemp(req, LoginUserContext.getUser().getId()));
    }
}