package com.anyi.sparrow.commission;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.EnumUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.commission.dto.income.*;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionSettleGainType;
import com.anyi.common.commission.req.IncomeDataDailyBizDetailReq;
import com.anyi.common.commission.req.IncomeDataDailyDetailReq;
import com.anyi.common.commission.req.IncomeDataDailyTotalReq;
import com.anyi.common.commission.req.IncomeStatReq;
import com.anyi.common.commission.response.IncomeDataDailyBizDetailVO;
import com.anyi.common.commission.response.IncomeDataDailyDetailVO;
import com.anyi.common.commission.response.IncomeDataDailyTotalVO;
import com.anyi.common.commission.service.CommissionSettleDataService;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.domain.param.Response;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.util.MoneyUtil;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.common.req.PageReq;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * APP我的收益相关接口
 * </p>
 *
 * @author shenbh
 * @since 2023.03.13
 */
@Slf4j
@Api(tags = "APP我的收益")
@RestController
@RequestMapping("eapp/commission/v1.0")
public class CommissionIncomeController {
    @Autowired
    private CommissionSettleService commissionSettleService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CommissionSettleDataService commissionSettleDataService;


    /**
     * 平台服务费收益统计接口
     *
     * @return 实例对象
     */
    @ApiOperation("交易服务费收益统计接口")
    @PostMapping("/income/plat/personStats")
    @WebLog(description = "交易服务费收益统计接口")
    public Response<PersonStatDTO> platPersonStats() {
        LoginUser currentUser = LoginUserContext.getUser();
        CommissionBizType bizType = CommissionBizType.PLAT_SERVICE;
        PersonStatDTO dto = commissionSettleService.personStats(bizType, currentUser.getId());
        return Response.ok(dto);
    }

    /**
     * app拉新收益统计接口
     *
     * @return 实例对象
     */
    @ApiOperation("app拉新收益统计接口")
    @PostMapping("/income/app/personStats")
    @WebLog(description = "app拉新收益统计接口")
    public Response<PersonStatDTO> appPersonStats() {
        LoginUser currentUser = LoginUserContext.getUser();
        CommissionBizType bizType = CommissionBizType.APP_NEW;
        PersonStatDTO dto = commissionSettleService.personStats(bizType, currentUser.getId());
        return Response.ok(dto);
    }

    /**
     * app拉新收益统计接口
     *
     * @return 实例对象
     */
    @ApiOperation("手机回收收益统计接口")
    @PostMapping("/income/collect/personStats")
    @WebLog(description = "手机回收收益统计接口")
    public Response<PersonStatDTO> collectPersonStats() {
        LoginUser currentUser = LoginUserContext.getUser();
        CommissionBizType bizType = CommissionBizType.PHONE_DOWN;
        PersonStatDTO dto = commissionSettleService.personStats(bizType, currentUser.getId());
        return Response.ok(dto);
    }

    /**
     * 收益统计通用接口
     *
     * @return 实例对象
     */
    @ApiOperation("收益统计通用接口")
    @PostMapping("/income/common/personStats")
    @WebLog(description = "收益统计通用接口")
    public Response<PersonStatDTO> commonPersonStats(@RequestBody IncomeStatReq req) {
        LoginUser currentUser = LoginUserContext.getUser();
        CommissionBizType bizType = EnumUtil.getBy(CommissionBizType::getType, Long.valueOf(req.getBizType()));
        PersonStatDTO dto = commissionSettleService.personStats(bizType, currentUser.getId());
        return Response.ok(dto);
    }


    /**
     * 个人收益-待结算明细列表接口
     *
     * @return 实例对象
     */
    @ApiOperation("个人收益-待结算明细列表接口")
    @PostMapping("/income/person/waitSettles")
    public Response<PersonWaitSettlesDTO> personWaitSettles(@RequestBody PageReq pageReq) {
        LoginUser currentUser = LoginUserContext.getUser();

        Long waitSettleAcc = commissionSettleService.userWaitSettleAcc(currentUser.getId(), pageReq.getBizType());

        Page<WaitSettlesDTO> pageDto = commissionSettleService.issueWaitSettles(currentUser.getId(), pageReq);
        PersonWaitSettlesDTO dto = new PersonWaitSettlesDTO();
        dto.setAccWaitSettile(MoneyUtil.convert(waitSettleAcc));

        if (CollectionUtil.isNotEmpty(pageDto.getResult())) {
            dto.setList(pageDto.getResult());
        }

        Response<PersonWaitSettlesDTO> response = Response.ok(dto);
        response.setCount((int) pageDto.getTotal());
        return response;
    }

    /**
     * 我的团队-待结算收益明细列表接口
     *
     * @return 实例对象
     */
    @ApiOperation("我的团队-待结算收益明细列表接口")
    @PostMapping("/income/team/waitSettles")
    public Response<TeamWaitSettlesDTO> teamWaitSettles(@RequestBody PageReq pageReq) {
        LoginUser currentUser = LoginUserContext.getUser();

        Long waitSettleAcc = commissionSettleService.teamWaitSettleAcc(currentUser.getId(), pageReq.getBizType());

        Page<TeamWaitSettlesDetailDTO> pageDto = commissionSettleService.issueTeamWaitSettles(currentUser.getId(), pageReq);
        TeamWaitSettlesDTO dto = new TeamWaitSettlesDTO();
        dto.setAccWaitSettile(MoneyUtil.convert(waitSettleAcc));

        if (CollectionUtil.isNotEmpty(pageDto.getResult())) {
            dto.setList(pageDto.getResult());
        }

        Response<TeamWaitSettlesDTO> response = Response.ok(dto);
        response.setCount((int) pageDto.getTotal());
        return response;
    }


    @ApiOperation("收益统计")
    @PostMapping("/income/data/daily/total")
    public Response<IncomeDataDailyTotalVO> incomeDataDailyTotal(@RequestBody IncomeDataDailyTotalReq req) {
        IncomeDataDailyTotalVO vo = commissionSettleDataService.incomeDataDailyTotal(req.getMonth(), LoginUserContext.getUser().getId());
        return Response.ok(vo);
    }

    @ApiOperation("收益统计-结算明细")
    @PostMapping("/income/data/daily/detail")
    public Response<IncomeDataDailyDetailVO> incomeDataDailyDetail(@Validated @RequestBody IncomeDataDailyDetailReq req) {
        IncomeDataDailyDetailVO vo = commissionSettleDataService.incomeDataDailyDetail(req.getDay(), LoginUserContext.getUser().getId());
        return Response.ok(vo);
    }

    @ApiOperation("收益统计-结算明细详情")
    @PostMapping("/income/data/daily/biz/detail")
    public Response<IncomeDataDailyBizDetailVO> incomeDataDailyBizDetail(@Validated @RequestBody IncomeDataDailyBizDetailReq req) {
        IncomeDataDailyBizDetailVO vo = commissionSettleDataService.incomeDataDailyBizDetail(req.getId(), LoginUserContext.getUser().getId());
        return Response.ok(vo);
    }
}