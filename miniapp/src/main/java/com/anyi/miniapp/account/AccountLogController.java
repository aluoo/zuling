package com.anyi.miniapp.account;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.account.constant.UserFocusTypeEnum;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.dto.EmployeeAccoutLogDTO;
import com.anyi.common.account.dto.EmployeeAccoutLogStatDTO;
import com.anyi.common.account.req.*;
import com.anyi.common.account.service.IEmployeeAccountLogService;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.account.vo.EmployeeAccountVO;
import com.anyi.common.account.vo.CompanyEmployeeVO;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.company.service.RecycleRechargeLogService;
import com.anyi.common.domain.param.Response;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.result.DictMapVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.miniapp.company.service.RecycleCompanyService;
import com.anyi.miniapp.interceptor.UserManager;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 账户模块相关接口控制器
 * </p>
 *
 * @author chenjian
 * @since 2024-03-11
 */
@Slf4j
@Api(tags = "账户额度模块")
@RestController
@RequestMapping("/mobile/account")
public class AccountLogController {

    @Autowired
    private IEmployeeAccountLogService employeeAccountLogService;
    @Autowired
    private IEmployeeAccountService employeeAccountService;
    @Autowired
    private CommonSysDictService commonSysDictService;
    @Autowired
    private RecycleRechargeLogService recycleRechargeLogService;
    @Autowired
    private RecycleCompanyService recycleCompanyService;
    @Autowired
    private CompanyService companyService;

    @ApiOperation("回收商账户信息")
    @WebLog(description = "回收商账户信息")
    @PostMapping("getAccountInfo")
    public Response<CompanyEmployeeVO> getAccountInfo() {
        return Response.ok(recycleCompanyService.getCompanyEmployee(UserManager.getCurrentUser().getCompanyId(), UserManager.getCurrentUser().getEmployeeId()));
    }

    @ApiOperation("获取资金明细类型接口")
    @WebLog(description = "获取资金明细类型接口")
    @PostMapping("getAccountLogType")
    public Response<List<Map<String, String>>> getAccountLogType() {
        List<Map<String, String>> list = UserFocusTypeEnum.getTypesForUserSelect();
        return Response.ok(list);
    }

    @ApiOperation("获取资金钱包接口")
    @WebLog(description = "获取资金钱包接口")
    @PostMapping("getAccount")
    public Response<EmployeeAccountVO> getAccount(@RequestBody QueryAccountReq req) {
        Company company = companyService.getById(UserManager.getCurrentUser().getCompanyId());
        return Response.ok(employeeAccountService.getByEmployee(company.getEmployeeId()));
    }


    @ApiOperation("资金明细列表接口")
    @WebLog(description = "资金明细列表接口")
    @PostMapping("listAccountLog")
    public Response<List<EmployeeAccoutLogDTO>> listAccountLog(@RequestBody QueryAccountLogReq req) {
        Company company = companyService.getById(UserManager.getCurrentUser().getCompanyId());
        Page<EmployeeAccountLog> pageDto = employeeAccountLogService.listUserFocusAccountLog(company.getEmployeeId(), req);
        List<EmployeeAccountLog> list = pageDto.getResult();

        List<EmployeeAccoutLogDTO> result = null;
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(item -> mappingToDTO(item)).collect(Collectors.toList());
        } else {
            result = new ArrayList<>();
        }
        Response<List<EmployeeAccoutLogDTO>> response = Response.ok(result);
        response.setCount((int) pageDto.getTotal());
        return response;
    }

    @ApiOperation("资金明细统计接口")
    @WebLog(description = "资金明细统计接口")
    @PostMapping("listAccountSum")
    public Response<EmployeeAccoutLogStatDTO> accountLogSum(@RequestBody QueryAccountLogReq req) {
        Company company = companyService.getById(UserManager.getCurrentUser().getCompanyId());
        return Response.ok(employeeAccountLogService.AccountLogSum(company.getEmployeeId(), req));
    }

    @ApiOperation("服务商账户充值获取")
    @WebLog(description = "服务商账户充值获取")
    @GetMapping(value = "/getRecharge")
    public Response<DictMapVO> getRecharge() {
        return Response.ok(commonSysDictService.getNameMap("recycle_charge_account"));
    }

    @ApiOperation("账户充值")
    @WebLog(description = "账户充值")
    @PostMapping("recharge")
    public Response accountLogSum(@RequestBody @Validated AccountRechargeReq req) {
        req.setCompanyId(UserManager.getCurrentUser().getCompanyId());
        recycleRechargeLogService.saveRecharge(req);
        return Response.ok();
    }

    @ApiOperation("报价师列表")
    @WebLog(description = "报价师列表")
    @PostMapping("employeeList")
    public Response<List<Employee>> employeeList() {
        List<Employee> resultList = recycleCompanyService.getEmployeeList(UserManager.getCurrentUser().getCompanyId());
        return Response.ok(resultList);
    }

    @ApiOperation("修改报价师")
    @WebLog(description = "修改报价师")
    @PostMapping("updateEmployee")
    public Response updateEmployee(@RequestBody @Validated UpdateEmployeeReq req) {
        recycleCompanyService.updateEmployee(req);
        return Response.ok();
    }

    @ApiOperation("注销报价师")
    @WebLog(description = "注销报价师")
    @PostMapping("cancelEmployee")
    public Response employeeList(@RequestBody @Validated CancelEmployeeReq req) {
        recycleCompanyService.cancel(req);
        return Response.ok();
    }


    public EmployeeAccoutLogDTO mappingToDTO(EmployeeAccountLog employeeAccountLog) {
        if (employeeAccountLog == null) {
            return null;
        }
        EmployeeAccoutLogDTO employeeAccoutLogDTO = new EmployeeAccoutLogDTO();
        employeeAccoutLogDTO.setLogId(employeeAccountLog.getId());
        employeeAccoutLogDTO.setChangeBalance(employeeAccountLog.getChangeBalance());
        employeeAccoutLogDTO.setChangeDetailType(employeeAccountLog.getChangeDetailType());
        employeeAccoutLogDTO.setAbleBalanceChangeAfter(employeeAccountLog.getAbleBalanceAfter());
        employeeAccoutLogDTO.setChangeMainType(employeeAccountLog.getChangeMainType());
        employeeAccoutLogDTO.setAbleBalanceChange(employeeAccountLog.getAbleBalanceChange());
        employeeAccoutLogDTO.setCorrelationId(employeeAccountLog.getCorrelationId());
        employeeAccoutLogDTO.setCreateTime(employeeAccountLog.getCreateTime());
        employeeAccoutLogDTO.setTitle(employeeAccountLog.getRemark());

        return employeeAccoutLogDTO;
    }


}
