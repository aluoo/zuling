package com.anyi.sparrow.organize.employee.controller;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.common.utils.ValidatorUtil;
import com.anyi.sparrow.organize.employee.req.EmployeeMobileQueryReq;
import com.anyi.sparrow.organize.employee.service.EmManagerService;
import com.anyi.sparrow.organize.employee.service.EmployeeSearchService;
import com.anyi.sparrow.organize.employee.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@Api(tags = "员工管理接口-最新版本")
@Validated
@RequestMapping("eapp/manage")
public class StaffManageController {
    @Autowired
    private EmManagerService emManagerService;
    @Autowired
    private EmployeeSearchService employeeSearchService;

    @PostMapping("v1.0/createStaff")
    @ApiOperation("创建员工")
    public Response createEm(@RequestBody@Validated@Valid EmInfoReq emInfoReq){
        emManagerService.createEm(emInfoReq.getMobileNumber(), emInfoReq.getName(), emInfoReq.getDeptId());
        return Response.ok();
    }
    @PostMapping("v1.0/updateStaff")
    @ApiOperation("更新员工")
    public Response updateStaff(@RequestBody@Validated@Valid UpdateEmReq emInfoReq){
        emManagerService.updateEm(emInfoReq);
        return Response.ok();
    }

    @ApiOperation("更新头像")
    @RequestMapping(value = "v1.0/updateHeadUrl", method = RequestMethod.POST)
    public Response<?> updateHeadUrl(@RequestBody @Validated UpdateHeadUrlReq req) {
        emManagerService.updateHeadUrl(req.getHeadUrl());
        return Response.ok();
    }

    @PostMapping("v1.0/updateName")
    @ApiOperation("更新员工名字")
    public Response updateName(String name){
        if(!Pattern.matches("^[\\u4E00-\\u9FA5A-Za-z0-9_-]+$", name)){
            throw new BusinessException(BizError.EMPLOYEE_NAME_ERROR);
        }
        emManagerService.updateName(name);
        return Response.ok();
    }

    @PostMapping("v1.0/updateDept")
    @ApiOperation("更新部门信息")
    public Response<DeptRes> updateDept(@RequestBody@Valid@Validated UpdateDeptReq updateDeptReq){
        return Response.ok(emManagerService.updateDept(updateDeptReq));
    }
    @PostMapping("v1.0/createDept")
    @ApiOperation("创建部门")
    public Response<DeptRes> createDept(@RequestBody @Validated @Valid CreateDeptReq createDeptReq){
        return Response.ok(emManagerService.createDept(createDeptReq));
    }
    @PostMapping("v1.0/createChannel")
    @ApiOperation("创建渠道")
    public Response createChannel(@RequestBody@Valid@Validated CreateChannelReq createChannelReq){
        emManagerService.createChannel(createChannelReq);
        return Response.ok();
    }
    @PostMapping("v1.0/updateChannel")
    @ApiOperation("更新渠道信息")
    public Response updateChannel(@RequestBody @Validated @Valid UpdateChannelReq req){
        emManagerService.updateChannel(req);
        return Response.ok();
    }
    @PostMapping("v1.0/deleteStaff")
    @ApiOperation("注销员工")
    public Response deleteStaff(@RequestParam Long emId){
        emManagerService.deleteEm(emId);
        return Response.ok();
    }
    @PostMapping("v1.0/deleteDept")
    @ApiOperation("删除部门")
    public Response deleteDept(@RequestParam Long deptId){
        emManagerService.deleteDept(deptId);
        return Response.ok();
    }

    @GetMapping("v1.0/queryDeptList")
    @ApiOperation("查询g管理范围部门列表")
    @WebLog(description = "查询g管理范围部门列表")
    public Response<List<DeptListRs>> queryDeptList(){
        return Response.ok(emManagerService.queryDeptList());
    }

    @GetMapping("v1.0/queryDeptChildList")
    @ApiOperation("查询部门下人员管理关系")
    @WebLog(description = "查询部门下人员管理关系")
    public Response<QueryChildRes> queryDeptChildList(@RequestParam(required = false) @ApiParam("部门id, 不传查询自己部门")Long deptId){
        return Response.ok(emManagerService.queryDeptChildList(deptId));
    }

    @GetMapping("v1.0/queryCompanyChildList")
    @ApiOperation("查询公司下人员管理关系")
    @WebLog(description = "查询公司下人员管理关系")
    public Response<QueryChildRes> queryCompanyChildList(@RequestParam(required = true) @ApiParam(value = "公司id", required = true)Long companyId){
        return Response.ok(emManagerService.queryCompanyChildList(companyId));
    }

    @GetMapping("v1.0/queryByNameOrMobile")
    @ApiOperation("姓名或手机号模糊查询员工信息")
    @WebLog(description = "姓名或手机号模糊查询员工信息")
    public Response<List<EmInfo>> queryByNameOrMobile(@RequestParam String express){
        return Response.ok(emManagerService.queryByNameOrMobile(express));
    }

    @PostMapping("v1.0/query/children/by/mobile")
    @ApiOperation("根据手机号查询员工及上级信息")
    @WebLog(description = "根据手机号查询员工及上级信息")
    public Response<List<EmployeeQueryVO>> queryChildrenByMobile(@RequestBody EmployeeMobileQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(employeeSearchService.queryChildrenByMobile(req.getMobile()));
    }
}