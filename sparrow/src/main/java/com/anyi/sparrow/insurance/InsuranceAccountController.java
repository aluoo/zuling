package com.anyi.sparrow.insurance;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.account.req.AccountRechargeReq;
import com.anyi.common.account.req.QueryAccountLogReq;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.insurance.constant.CompanyUserFocusTypeEnum;
import com.anyi.common.insurance.domain.DiCompanyAccountLog;
import com.anyi.common.insurance.domain.DiCompanyRechargeLog;
import com.anyi.common.insurance.req.InsuranceEmployeePriceReq;
import com.anyi.common.insurance.response.CompanyAccountLogDTO;
import com.anyi.common.insurance.response.DiSkuInsuranceVO;
import com.anyi.common.insurance.response.InsuranceAccountVO;
import com.anyi.common.insurance.service.DiCompanyAccountLogService;
import com.anyi.common.insurance.service.DiCompanyAccountService;
import com.anyi.common.insurance.service.DiCompanyRechargeLogService;
import com.anyi.common.result.DictMapVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.insurance.service.InsuranceEmployeePriceService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "碎屏保-门店账户")
@RequestMapping("/insurance/account")
public class InsuranceAccountController {

    @Autowired
    DiCompanyAccountService companyAccountService;
    @Autowired
    DiCompanyAccountLogService diCompanyAccountLogService;
    @Autowired
    CommonSysDictService commonSysDictService;
    @Autowired
    DiCompanyRechargeLogService diCompanyRechargeLogService;
    @Autowired
    InsuranceEmployeePriceService employeePriceService;


    @GetMapping("/info")
    @WebLog(description = "资金账户信息")
    @ApiOperation("资金账户信息")
    public Response<InsuranceAccountVO> getInfo() {
        return Response.ok(companyAccountService.getByCompany(LoginUserContext.getUser().getCompanyId()));
    }

    @ApiOperation("获取资金明细类型接口")
    @WebLog(description = "获取资金明细类型接口")
    @PostMapping("getAccontLogType")
    public Response<List<Map<String, String>>> getAccontLogType() {
        List<Map<String, String>> list = CompanyUserFocusTypeEnum.getTypesForUserSelect();
        return Response.ok(list);
    }

    @ApiOperation("资金明细列表接口")
    @WebLog(description = "资金明细列表接口")
    @PostMapping("listAccontLog")
    public Response<List<CompanyAccountLogDTO>> listAccontLog(@RequestBody QueryAccountLogReq req) {
        Long companyId = LoginUserContext.getUser().getCompanyId();
        if (ObjectUtil.isNotNull(req.getEmployeeId())) {
            companyId = req.getEmployeeId();
        }
        Page<DiCompanyAccountLog> pageDto = diCompanyAccountLogService.listUserFocusAccountLog(companyId, req);
        List<DiCompanyAccountLog> list = pageDto.getResult();

        List<CompanyAccountLogDTO> result = null;
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.stream().map(item -> mapppingToDTO(item)).collect(Collectors.toList());
        } else {
            result = new ArrayList<>();
        }
        Response<List<CompanyAccountLogDTO>> response = Response.ok(result);
        response.setCount((int) pageDto.getTotal());
        return response;
    }

    public CompanyAccountLogDTO mapppingToDTO(DiCompanyAccountLog employeeAccountLog) {
        if (employeeAccountLog == null) {
            return null;
        }
        CompanyAccountLogDTO employeeAccoutLogDTO = new CompanyAccountLogDTO();
        employeeAccoutLogDTO.setLogId(employeeAccountLog.getId());
        employeeAccoutLogDTO.setChangeMainType(employeeAccountLog.getChangeMainType());
        employeeAccoutLogDTO.setChangeDetailType(employeeAccountLog.getChangeDetailType());
        employeeAccoutLogDTO.setAbleBalanceChangeAfter(employeeAccountLog.getAbleBalanceAfter());
        employeeAccoutLogDTO.setAbleBalanceChange(employeeAccountLog.getAbleBalanceChange());
        employeeAccoutLogDTO.setChangeBalance(employeeAccountLog.getChangeBalance());
        employeeAccoutLogDTO.setCorrelationId(employeeAccountLog.getCorrelationId());
        employeeAccoutLogDTO.setCreateTime(employeeAccountLog.getCreateTime());
        employeeAccoutLogDTO.setTitle(employeeAccountLog.getRemark());

        return employeeAccoutLogDTO;
    }

    @ApiOperation("账户充值获取")
    @WebLog(description = "账户充值获取")
    @GetMapping(value = "/getRecharge")
    public Response<DictMapVO> getRecharge() {
        return Response.ok(commonSysDictService.getNameMap("recycle_charge_account"));
    }

    @ApiOperation("账户充值")
    @WebLog(description = "账户充值")
    @PostMapping("recharge")
    public Response recharge(@RequestBody @Validated AccountRechargeReq req) {
        req.setCompanyId(LoginUserContext.getUser().getCompanyId());
        diCompanyRechargeLogService.saveRecharge(req);
        return Response.ok();
    }

    @ApiOperation("充值账单")
    @WebLog(description = "充值账单")
    @PostMapping(value = "/rechargeList")
    public Response<List<DiCompanyRechargeLog>> rechargeList() {
        return Response.ok(diCompanyRechargeLogService
                .rechargeList(LoginUserContext.getUser().getCompanyId()));
    }

    @ApiOperation("充值账单详情")
    @WebLog(description = "充值账单详情")
    @GetMapping(value = "/rechargeDetail")
    public Response<DiCompanyRechargeLog> rechargeDetail(Long id) {
        return Response.ok(diCompanyRechargeLogService.getById(id));
    }

    @ApiOperation("数保价格查询")
    @WebLog(description = "数保价格查询")
    @GetMapping(value = "/insurance/price")
    public Response<DiSkuInsuranceVO> insurancePrice(InsuranceEmployeePriceReq req) {
        req.setEmployeeId(LoginUserContext.getUser().getId());
        return Response.ok(employeePriceService.insuranceBenefit(req));
    }

}