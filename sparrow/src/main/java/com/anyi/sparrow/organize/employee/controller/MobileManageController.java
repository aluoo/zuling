package com.anyi.sparrow.organize.employee.controller;

import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.company.dto.CompanyDTO;
import com.anyi.common.company.dto.PackageInfoApplyDTO;
import com.anyi.common.company.dto.PackageInfoDTO;
import com.anyi.common.company.req.CompanyPackageReq;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.company.service.PackageInfoService;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.company.vo.RecycleCompanyVO;
import com.anyi.common.domain.param.Response;
import com.anyi.common.employee.req.AgencyDTO;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.employee.vo.AgencyVO;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.employee.service.EmManagerService;
import com.anyi.sparrow.organize.employee.service.EmployeeSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "二手机代理模块")
@Validated
@RequestMapping("mobile/manage")
public class MobileManageController {
    @Autowired
    private EmManagerService emManagerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeSearchService employeeSearchService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PackageInfoService packageInfoService;

    @PostMapping("/agency")
    @WebLog(description = "代理一级界面接口")
    @ApiOperation("代理一级界面接口")
    public Response<AgencyVO> AgencyFirst() {
        AgencyVO resultVo = employeeService.agencyFirst(LoginUserContext.getUser().getAncestors(), LoginUserContext.getUser().getLevel());
        return Response.ok(resultVo);
    }


    @PostMapping("/netxAgency")
    @WebLog(description = "代理二级界面接口")
    @ApiOperation("代理二级界面接口")
    public Response<AgencyVO> nextAgency(@RequestBody @Validated AgencyDTO agencyDTO) {
        AgencyVO resultVo = employeeService.agencyFirst(agencyDTO.getAncestors(), agencyDTO.getLevel());
        return Response.ok(resultVo);
    }

    @PostMapping("/groupCompany")
    @WebLog(description = "团队门店页面")
    @ApiOperation("团队门店页面")
    public Response<List<AgencyCompanyVO>> groupCompany(@RequestBody CompanyReq req) {
        List<AgencyCompanyVO> resultVo = companyService.ancestorsCompany(req);
        return Response.ok(resultVo);
    }

    @GetMapping("/company/detail")
    @WebLog(description = "门店详情")
    @ApiOperation("门店详情")
    public Response<AgencyCompanyVO> companyDetail(Long companyId) {
        AgencyCompanyVO resultVo = companyService.companyDetail(companyId);
        return Response.ok(resultVo);
    }

    @PostMapping("/company")
    @WebLog(description = "门店页面")
    @ApiOperation("门店页面")
    public Response<List<AgencyCompanyVO>> company(@RequestBody CompanyReq req) {
        req.setAncestors(LoginUserContext.getUser().getAncestors());
        List<AgencyCompanyVO> resultVo = companyService.ancestorsCompany(req);
        return Response.ok(resultVo);
    }

    @PostMapping("/company/update")
    @WebLog(description = "修改门店信息")
    @ApiOperation("修改门店信息")
    public Response<List<AgencyCompanyVO>> companyUpdate(@RequestBody CompanyDTO companyDTO) {
        companyService.updateCompany(companyDTO);
        return Response.ok();
    }

    @PostMapping("/recycle/company")
    @WebLog(description = "回收商列表")
    @ApiOperation("回收商列表")
    public Response<List<RecycleCompanyVO>> recycle(@RequestBody CompanyReq req) {
        if(LoginUserContext.getUser().getLevel()!=0){
            throw new BusinessException(99999,"暂无权限");
        }
        return Response.ok(companyService.recycleList(req));
    }


    @ApiOperation("门店压价列表")
    @WebLog(description = "门店压价列表")
    @PostMapping("/company/package")
    public Response<List<PackageInfoDTO>> companyUpdate(@RequestBody CompanyPackageReq companyPackageReq) {
        if (ObjectUtil.isNull(companyPackageReq.getCompanyId())) {
            companyPackageReq.setCompanyId(LoginUserContext.getUser().getCompanyId());
        }
        return Response.ok(packageInfoService.packageInfoList(companyPackageReq));
    }

    @PostMapping("/company/packageSet")
    @WebLog(description = "门店压价设置")
    @ApiOperation("门店压价设置")
    public Response companyUpdate(@RequestBody @Validated List<PackageInfoApplyDTO> packageList) {
        packageInfoService.packageSet(packageList);
        return Response.ok();
    }

    /**
     * 直营连锁或者门店(代理直接发展的)
     * @param req
     * @return
     */
    @PostMapping("/dictCompany")
    @WebLog(description = "直营门店页面")
    @ApiOperation("直营门店页面")
    public Response<List<AgencyCompanyVO>> dictCompany(@RequestBody CompanyReq req) {
        List<AgencyCompanyVO> resultVo = companyService.dictCompany(req);
        return Response.ok(resultVo);
    }

}