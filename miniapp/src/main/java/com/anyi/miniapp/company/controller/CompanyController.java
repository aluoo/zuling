package com.anyi.miniapp.company.controller;


import com.anyi.common.aspect.RepeatSubmit;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.company.dto.CompanyDTO;
import com.anyi.common.company.dto.EmployeeApplyDTO;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.param.Response;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.miniapp.interceptor.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "服务商和门店加入")
@RestController
@Slf4j
public class CompanyController {

    @Autowired
    CompanyService companyService;
    @Autowired
    EmployeeService employeeService;


    @ApiOperation("门店加入")
    @WebLog(description = "门店加入")
    @ResponseBody
    @PostMapping("/company/join")
    @RepeatSubmit
    public Response joinCompany(@RequestBody @Validated CompanyDTO companyDTO) {
        companyDTO.setUserId(UserManager.getUserId());
        companyService.joinCompany(companyDTO);
        return Response.ok();
    }

    @ApiOperation("门店员工邀请加入")
    @WebLog(description = "门店员工邀请加入")
    @ResponseBody
    @PostMapping("/company/invite")
    @RepeatSubmit
    public Response inviteEmployee(@RequestBody @Validated EmployeeApplyDTO applyDTO) {
        applyDTO.setName(UserManager.getMobile());
        applyDTO.setMobileNumber(UserManager.getMobile());
        employeeService.createEmployee(applyDTO);
        return Response.ok();
    }

    @ApiOperation("门店员工加入")
    @WebLog(description = "门店员工加入")
    @ResponseBody
    @PostMapping("/company/staff/join")
    @RepeatSubmit
    public Response<?> joinCompanyStaff(@RequestBody @Validated EmployeeApplyDTO applyDTO) {
        return Response.ok(employeeService.createEmployee(applyDTO));
    }
}