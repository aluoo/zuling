package com.anyi.sparrow.withdraw.controller;


import cn.hutool.core.util.StrUtil;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.common.qfu.QfuService;
import com.anyi.common.withdraw.domain.CommonWithdrawEmployeeApply;
import com.anyi.common.withdraw.service.CommonWithdrawEmployeeApplyService;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.withdraw.dto.*;
import com.anyi.sparrow.withdraw.req.InvoiceApplyReq;
import com.anyi.sparrow.withdraw.req.NormalApplyReq;
import com.anyi.sparrow.withdraw.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 提现模块相关接口控制器
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
@Slf4j
@Api(tags = "提现模块相关接口")
@RestController
@RequestMapping("/eapp/v1.0/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    QfuService qfuService;
    @Autowired
    private CommonWithdrawEmployeeApplyService withdrawEmployeeApplyService;

    @ApiOperation("可提现金额页面数据接口")
    @PostMapping("getIndexInfo")
    @WebLog(description = "可提现金额页面数据接口")
    public Response<WithdrawIndexDTO> getIndexInfo() {

        WithdrawIndexDTO dto = withdrawService.getAppWithdrawIndexData(LoginUserContext.getUser().getId());
        return Response.ok(dto);
    }

    @ApiOperation("提现申请提交前-获取确认金额接口")
    @PostMapping("calculateTax")
    @WebLog(description = "提现申请提交前-获取确认金额接口")
    public Response<AmountTaxDTO> calculateTax(@RequestBody @Validated NormalApplyReq req) {
        AmountTaxDTO dto = withdrawService.calculateTax(LoginUserContext.getUser().getId(), req);
        return Response.ok(dto);
    }

    @ApiOperation("获取发票邮寄地址接口")
    @PostMapping("getMailAddress")
    @WebLog(description = "获取发票邮寄地址接口")
    public Response<MailAdressDTO> getMailAddress() {
        MailAdressDTO dto = withdrawService.getMailAddress();
        return Response.ok(dto);
    }

    @ApiOperation("不能开票-提现申请提交接口")
    @PostMapping("createNormalApply")
    @WebLog(description = "不能开票-提现申请提交接口")
    public Response<ApplyIdDTO> createNormalApply(@RequestBody @Validated NormalApplyReq req) {
        ApplyIdDTO dto = withdrawService.createApply(LoginUserContext.getUser().getId(), req);
        return Response.ok(dto);
    }

    @ApiOperation("能开票-提交对公账户提现申请接口")
    @PostMapping("createInvoiceApply")
    @WebLog(description = "能开票-提交对公账户提现申请接口")
    public Response<ApplyIdDTO> createInvoiceApply(@RequestBody @Validated InvoiceApplyReq req) {
        ApplyIdDTO dto = withdrawService.createApply(LoginUserContext.getUser().getId(), req);
        return Response.ok(dto);
    }

    @ApiOperation("提现详情接口")
    @PostMapping("getApplyDetail")
    @WebLog(description = "提现详情接口")
    public Response<ApplyDetailDTO> getApplyDetail(@RequestBody @Validated ApplyIdDTO req) {
        ApplyDetailDTO dto = withdrawService.getApplyDetail(LoginUserContext.getUser().getId(), req.getApplyId());
        return Response.ok(dto);
    }

    @ApiOperation("青蚨查询")
    @RequestMapping(value = "/temp/qfu/check", method = RequestMethod.GET)
    @WebLog(description = "青蚨查询")
    public Response<?> tempQfuCheck(@RequestParam String order) {
        if (StrUtil.isBlank(order)) {
            return Response.failed(-1, "order 不能为空");
        }
        CommonWithdrawEmployeeApply apply = withdrawEmployeeApplyService.lambdaQuery().eq(CommonWithdrawEmployeeApply::getApplyNo, order).one();
        return Response.ok(qfuService.paymentCheck(order, apply.getType()));
    }
}