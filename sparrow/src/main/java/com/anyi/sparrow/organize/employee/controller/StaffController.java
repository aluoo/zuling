package com.anyi.sparrow.organize.employee.controller;

import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.organize.employee.req.PasswordUpdateReq;
import com.anyi.sparrow.organize.employee.service.EmManagerService;
import com.anyi.sparrow.organize.employee.service.EmService;
import com.anyi.sparrow.organize.employee.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "员工登录相关--新版本")
@RestController
@RequestMapping("eapp/staff")
public class StaffController {
    @Autowired
    private EmService service;

    @Autowired
    private EmManagerService emManagerService;

    @ApiOperation(value = "注册游客账号", notes = "临时用为了app过审")
    @ResponseBody
    @PostMapping("v1.0/temporary/register")
    public Response<?> temporaryRegister(@RequestBody @Validated @Valid TemporaryEmployeeLoginReq req) {
        service.temporaryRegister(req);
        return Response.ok();
    }

    @ApiOperation(value = "游客账号密码登录", notes = "临时用为了app过审")
    @ResponseBody
    @PostMapping("v1.0/temporary/login")
    public Response<EmployeeVO> temporaryLogin(@RequestBody @Validated @Valid TemporaryEmployeeLoginReq req) {
        return Response.ok(service.temporaryLogin(req));
    }

    @ApiOperation(value = "数保公众号登录")
    @ResponseBody
    @PostMapping("v1.0/gzhInsurance/login")
    public Response<EmployeeVO> gzhInsuranceLogin(@RequestBody @Validated @Valid TemporaryEmployeeLoginReq req) {
        return Response.ok(service.insuranceLogin(req));
    }

    @ApiOperation("登录")
    @ResponseBody
    @PostMapping("v1.0/login")
    public Response<EmployeeVO> login(@RequestBody @Validated @Valid EmployeeLoginVO vo) {
        return Response.ok(service.login(vo));
    }

    @ApiOperation("密码登录")
    @PostMapping("v1.0/pwd/login")
    public Response<EmployeeVO> loginByPwd(@RequestBody @Validated @Valid TemporaryEmployeeLoginReq vo) {
        return Response.ok(service.loginByPwd(vo));
    }

    @ApiOperation("获取登录验证码")
    @ResponseBody
    @GetMapping("v1.0/getLoginSmsCode")
    public Response getVerifyCode(@ApiParam("手机号") @RequestParam String mobile) {
        service.getVerifyCode(mobile);
        return Response.ok();
    }

    @ApiOperation("获取员工信息")
    @ResponseBody
    @GetMapping("v1.0/getUserInfo")
    public Response<EmployeeVO> getEmployee(@RequestParam(required = false) @ApiParam("员工id, 不传查询自己")Long emId) {
        return Response.ok(emManagerService.getEmployeeAdaptor(emId));
    }

    @ApiOperation("获取部门信息")
    @ResponseBody
    @GetMapping("v1.0/getDeptInfo")
    public Response<DeptVO> getDept(@RequestParam(required = false) @ApiParam("部门id, 不传查询自己的部门") Long deptId){
        return Response.ok(emManagerService.getDept(deptId));
    }

    @ApiOperation("获取公司信息")
    @ResponseBody
    @GetMapping("v1.0/getCompanyInfo")
    public Response<CompanyVO> getCompany(@RequestParam(required = false) @ApiParam("公司id, 不传查询公司")Long companyId) {
        return Response.ok(emManagerService.getCompany(companyId));
    }

    @ApiOperation("登出")
    @ResponseBody
    @PostMapping("v1.0/logout")
    public Response login() {
        service.logout();
        return Response.ok();
    }

    @ApiOperation("更改密码")
    @PostMapping("v1.0/updatePassword")
    public Response<?> updatePassword(@RequestBody PasswordUpdateReq req) {
        service.updatePassword(req);
        return Response.ok();
    }
}