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
import com.anyi.common.insurance.req.DiInsuranceBaseDTO;
import com.anyi.common.insurance.response.CompanyAccountLogDTO;
import com.anyi.common.insurance.response.DiBaseConfigVO;
import com.anyi.common.insurance.response.InsuranceAccountVO;
import com.anyi.common.insurance.service.*;
import com.anyi.common.result.DictMapVO;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.sparrow.base.security.LoginUserContext;
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
@Api(tags = "碎屏保-门店价格编辑")
@RequestMapping("/insurance/company")
public class InsuranceCompanyBaseController {

    @Autowired
    private DiCompanyProductBaseService companyProductBaseService;

    @ApiOperation("手机产品基本配置列表")
    @WebLog(description = "手机产品基本配置列表")
    @ResponseBody
    @GetMapping("/baseList/{productId}")
    public Response<List<DiBaseConfigVO>> baseList(@PathVariable Long productId ) {
        return Response.ok(companyProductBaseService.baseList(productId,LoginUserContext.getUser().getCompanyId()));
    }

    @ApiOperation("门店编辑数保零售价格")
    @WebLog(description = "门店编辑数保零售价格")
    @ResponseBody
    @GetMapping("/base/edit")
    public Response edit(@RequestBody DiInsuranceBaseDTO dto) {
        companyProductBaseService.saveBase(dto);
        return Response.ok();
    }










}