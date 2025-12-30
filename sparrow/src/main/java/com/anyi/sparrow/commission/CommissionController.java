package com.anyi.sparrow.commission;

import cn.hutool.json.JSONUtil;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.commission.dto.CmPlanPackageDTO;
import com.anyi.common.commission.dto.MemberDTO;
import com.anyi.common.commission.dto.OverviewDTO;
import com.anyi.common.commission.dto.PlanDTO;
import com.anyi.common.commission.req.CommissionSettleCheckReq;
import com.anyi.common.commission.req.PlanReq;
import com.anyi.common.commission.req.UpdatePlanReq;
import com.anyi.common.commission.response.CommissionSettleCheckSumVO;
import com.anyi.common.commission.response.CommissionSettleCheckVO;
import com.anyi.common.commission.service.CommissionSettleCheckService;
import com.anyi.common.commission.service.CommissionTypeService;
import com.anyi.common.commission.service.CommissionPlanService;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.service.PackageInfoService;
import com.anyi.common.domain.param.Response;
import com.anyi.common.insurance.response.InsuranceOrderDetailVO;
import com.anyi.common.product.domain.request.OrderQueryReq;
import com.anyi.sparrow.base.security.LoginUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "佣金方案")
@RestController
@RequestMapping("/eapp/commission/v1.0")
public class CommissionController {

    @Autowired
    private CommissionTypeService commissionBizTypeService;
    @Autowired
    private CommissionPlanService commissionPlanService;
    @Autowired
    private CommissionSettleCheckService commissionSettleCheckService;


    /**
     * 查询所有类型佣金方案数
     *
     * @return 实例对象
     */
    @ApiOperation("查询所有类型佣金方案")
    @WebLog(description = "查询所有类型佣金方案")
    @GetMapping("/overview")
    public Response<List<OverviewDTO>> overview() {
        log.info("【查询所有类型佣金方案数】");
        List<OverviewDTO> resultList = commissionBizTypeService.overview(LoginUserContext.getUser().getId());

        //代理没有压价方案
        if(LoginUserContext.getUser().getCompanyType().equals(CompanyType.COMPANY.getCode())){
            resultList = resultList.stream().filter(e -> e.getBizTypeId().intValue()!=3).collect(Collectors.toList());
        }else{
            resultList = resultList.stream().filter(e -> e.getBizTypeId().intValue()!=1).collect(Collectors.toList());
        }

        return Response.ok(resultList);
    }

    /**
     * 推广佣金方案列表
     *
     * @return 实例对象
     */
    @ApiOperation("推广佣金方案列表")
    @WebLog(description = "推广佣金方案列表")
    @GetMapping("/plan/list")
    public Response<List<PlanDTO>> planList(Long bizTypeId) {
        return Response.ok(commissionPlanService.planList(LoginUserContext.getUser().getId(), bizTypeId));
    }

    /**
     * 创建推广佣金方案
     *
     * @return 实例对象
     */
    @ApiOperation("创建佣金方案")
    @WebLog(description = "创建佣金方案")
    @PostMapping("/plan/create")
    public Response create(@RequestBody @Validated PlanReq planReq) {
        log.info("【创建佣金方案】{}", JSONUtil.toJsonStr(planReq));
        planReq.setEmployeeId(LoginUserContext.getUser().getId());
        commissionPlanService.create(planReq);
        return Response.ok();
    }

    /**
     * 查看推广佣金方案详情
     *
     * @return 实例对象
     */
    @ApiOperation("查看推广佣金方案详情")
    @WebLog(description = "查看推广佣金方案详情")
    @GetMapping("/plan/issue/detail/{planId}")
    public Response<Map<String, Object>> detail(@PathVariable("planId") Long planId) {
        log.info("【查看推广佣金方案详情】{}", planId);
        Map<String, Object> result = new HashMap<>();
        result.put("planConf", commissionPlanService.detail(LoginUserContext.getUser().getLevel(), LoginUserContext.getUser().getId(), planId));
        return Response.ok(result);
    }

    /**
     * 更新推广佣金方案接口
     *
     * @return 实例对象
     */
    @ApiOperation("更新推广佣金方案接口")
    @WebLog(description = "更新推广佣金方案接口")
    @PostMapping("plan/issue/update")
    public Response update(@RequestBody @Validated UpdatePlanReq updatePlanReq) {
        log.info("【更新推广佣金方案接口】{}", JSONUtil.toJsonStr(updatePlanReq));
        updatePlanReq.setEmployeeId(LoginUserContext.getUser().getId());
        commissionPlanService.update(updatePlanReq);
        return Response.ok();
    }

    /**
     * 查看推广佣金方案详情
     *
     * @return 实例对象
     */
    @ApiOperation("删除推广佣金方案接口")
    @WebLog(description = "删除推广佣金方案接口")
    @DeleteMapping("/plan/issue/delete/{planId}")
    public Response delete(@PathVariable("planId") Long planId) {
        log.info("【删除推广佣金方案接口】{}", planId);
        commissionPlanService.delete(LoginUserContext.getUser().getId(), planId);
        return Response.ok();
    }

    /**
     * 查看佣金方案已添加成员列表接口
     *
     * @return 实例对象
     */
    @ApiOperation("查看佣金方案已添加成员列表接口")
    @WebLog(description = "查看佣金方案已添加成员列表接口")
    @GetMapping("/plan/members/{planId}")
    public Response<MemberDTO> members(@PathVariable("planId") Long planId) {
        log.info("【查看佣金方案已添加成员列表接口】{}", planId);
        return Response.ok(commissionPlanService.members(LoginUserContext.getUser().getId(), planId));
    }

    /**
     * 删除佣金方案成员
     *
     * @return 实例对象
     */
    @ApiOperation("删除佣金方案成员")
    @WebLog(description = "删除佣金方案成员")
    @DeleteMapping("/plan/members/remove/{planId}")
    public Response<MemberDTO> removeMembers(@PathVariable("planId") Long planId, @RequestBody List<Long> delMembers) {
        log.info("【删除佣金方案成员】planId:{},delMembers:{}", planId, JSONUtil.toJsonStr(delMembers));
        commissionPlanService.removeMembers(LoginUserContext.getUser().getId(), planId, delMembers);
        return Response.ok();
    }


    /**
     * 添加佣金方案成员
     *
     * @return 实例对象
     */
    @ApiOperation("添加佣金方案成员")
    @WebLog(description = "添加佣金方案成员")
    @PostMapping("/plan/members/add/{planId}")
    public Response<MemberDTO> addMembers(@PathVariable("planId") Long planId, @RequestBody List<Long> addMembers) {
        log.info("【添加佣金方案成员】planId:{},addMembers:{}", planId, JSONUtil.toJsonStr(addMembers));
        commissionPlanService.addMembers(LoginUserContext.getUser().getId(), planId, addMembers);
        return Response.ok();
    }

    @ApiOperation("获取能给下级配置的套餐")
    @WebLog(description = "获取能给下级配置的套餐")
    @GetMapping("/plan/issue/getDefaut/{bizTypeId}")
    public Response<List<CmPlanPackageDTO>> getChildIssueConf(@PathVariable("bizTypeId") Long bizTypeId) {
        return Response.ok(commissionPlanService.getChildIssueConf(LoginUserContext.getUser().getId(), bizTypeId));
    }

    @ApiOperation("获取能给下级配置的套餐")
    @WebLog(description = "获取能给下级配置的套餐")
    @GetMapping("/plan/issue/admin/getDefaut/{bizTypeId}")
    public Response<List<CmPlanPackageDTO>> getAdminChildIssueConf(@PathVariable("bizTypeId") Long bizTypeId) {
        return Response.ok(commissionPlanService.getAdminChildIssueConf(LoginUserContext.getUser().getId(), bizTypeId));
    }

    /**
     * 共用
     *
     * @return
     */
    @ApiOperation("当前用户直接下级员工列表")
    @WebLog(description = "当前用户直接下级员工列表")
    @GetMapping("/plan/members/available")
    public Response<MemberDTO> childMembers(@RequestParam Long bizTypeId) {
        return Response.ok(commissionPlanService.childMembers(LoginUserContext.getUser().getId(), bizTypeId));
    }

    @ApiOperation("合伙人账单")
    @WebLog(description = "合伙人账单")
    @RequestMapping(value = "/bdCheck", method = RequestMethod.POST)
    public Response<List<CommissionSettleCheckVO>> listOrder(@RequestBody CommissionSettleCheckReq req) {
        req.setEmployeeId(LoginUserContext.getUser().getId());
        return Response.ok(commissionSettleCheckService.listOrder(req));
    }

    @ApiOperation("合伙人账单统计")
    @WebLog(description = "合伙人账单统计")
    @RequestMapping(value = "/bdCheckSum", method = RequestMethod.POST)
    public Response<CommissionSettleCheckSumVO> SumByParam(@RequestBody CommissionSettleCheckReq req) {
        req.setEmployeeId(LoginUserContext.getUser().getId());
        return Response.ok(commissionSettleCheckService.SumByParam(req));
    }



}
