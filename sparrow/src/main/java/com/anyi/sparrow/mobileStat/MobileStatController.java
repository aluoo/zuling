package com.anyi.sparrow.mobileStat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.anyi.common.mobileStat.response.RecycleStatVO;
import com.anyi.common.mobileStat.service.CompanyDataDailyBaseService;
import com.anyi.common.mobileStat.service.RecycleDataDailyBaseService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.mobileStat.req.CompanyStatReq;
import com.anyi.sparrow.mobileStat.response.CompanyDirectListVO;
import com.anyi.sparrow.mobileStat.response.CompanyStatAgentVO;
import com.anyi.sparrow.organize.employee.service.EmManagerService;
import com.anyi.sparrow.organize.employee.service.EmployeeSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "二手机统计模块")
@Validated
@RequestMapping("mobile/stat")
public class MobileStatController {
    @Autowired
    private CompanyDataDailyBaseService companyDataDailyBaseService;
    @Autowired
    private RecycleDataDailyBaseService recycleDataDailyBaseService;
    @Autowired
    private CompanyStatService companyStatService;

    @PostMapping("/company")
    @WebLog(description = "门店统计")
    @ApiOperation("门店统计")
    public Response<CompanyStatVO> companyStat(@RequestBody CompanyStatDTO req) {
        req.setAncestors(LoginUserContext.getUser().getAncestors());
        CompanyStatVO resultVo = new CompanyStatVO();
        resultVo = companyDataDailyBaseService.companyStatToday(req);
        /*if(req.getStartTime()!=null && req.getEndTime()!=null && DateUtil.isSameDay(req.getStartTime(),req.getEndTime()) && DateUtil.isSameDay(req.getStartTime(),new Date())){
            resultVo = companyDataDailyBaseService.companyStatToday(req);
        }else{
            resultVo = companyDataDailyBaseService.companyStat(req);
        }*/
        return Response.ok(resultVo);
    }

    @PostMapping("/person")
    @WebLog(description = "个人统计")
    @ApiOperation("个人统计")
    public Response<CompanyStatVO> personStat(@RequestBody CompanyStatDTO req) {
        if(req.getEmployeeId()==null){
            req.setEmployeeId(LoginUserContext.getUser().getId());
        }
        CompanyStatVO resultVo = companyDataDailyBaseService.personStat(req);
        /*if(req.getStartTime()!=null && req.getEndTime()!=null && DateUtil.isSameDay(req.getStartTime(),req.getEndTime()) && DateUtil.isSameDay(req.getStartTime(),new Date())){
            resultVo = companyDataDailyBaseService.companyStatToday(req);
        }else{
            resultVo = companyDataDailyBaseService.companyStat(req);
        }*/
        return Response.ok(resultVo);
    }

    @PostMapping("/agency")
    @WebLog(description = "门店统计一级界面接口")
    @ApiOperation("门店统计一级界面接口")
    public Response<CompanyStatAgentVO> AgencyFirst(@RequestBody CompanyStatReq req) {
        req.setAncestors(LoginUserContext.getUser().getAncestors());
        req.setLevel(LoginUserContext.getUser().getLevel());
        return Response.ok(companyStatService.agencyFirst(req));
    }


    @PostMapping("/netxAgency")
    @WebLog(description = "门店统计二级界面接口")
    @ApiOperation("门店统计二级界面接口")
    public Response<CompanyStatAgentVO> nextAgency(@RequestBody CompanyStatReq req) {
        return Response.ok(companyStatService.agencyFirst(req));
    }


    @PostMapping("/dictCompany")
    @WebLog(description = "直营门店统计")
    @ApiOperation("直营门店统计")
    public Response<CompanyDirectListVO> dictCompany(@RequestBody CompanyStatReq req) {
        return Response.ok(companyStatService.dictCompany(req));
    }

    @PostMapping("/groupCompany")
    @WebLog(description = "团队门店统计")
    @ApiOperation("团队门店统计")
    public Response<CompanyDirectListVO> groupCompany(@RequestBody CompanyStatReq req) {
        return Response.ok(companyStatService.groupCompany(req));
    }


    @PostMapping("/companyRole")
    @WebLog(description = "门店和连锁用户角色统计")
    @ApiOperation("门店和连锁用户角色统计")
    public Response<CompanyStatAgentVO> companyRole(@RequestBody CompanyStatReq req) {

        if(StrUtil.isBlank(req.getAncestors())){
            req.setAncestors(LoginUserContext.getUser().getAncestors());
        }
        if(ObjectUtil.isNull(req.getLevel())){
            req.setLevel(LoginUserContext.getUser().getLevel());
        }

        return Response.ok(companyStatService.companyFirst(req));
    }



    @PostMapping("/recycle")
    @WebLog(description = "回收商统计")
    @ApiOperation("回收商统计")
    public Response<RecycleStatVO> recycleStat(@RequestBody CompanyStatDTO req) {
        //没传默认看当前登陆人底下
        req.setAncestors(LoginUserContext.getUser().getAncestors());
        RecycleStatVO resultVo = recycleDataDailyBaseService.recycleStat(req);
        return Response.ok(resultVo);
    }


}