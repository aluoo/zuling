package com.anyi.sparrow.withdraw.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;

import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import com.anyi.sparrow.withdraw.dto.CardInfoDTO;
import com.anyi.sparrow.withdraw.req.CardIdReq;
import com.anyi.sparrow.withdraw.req.PublicAccountBindReq;
import com.anyi.sparrow.withdraw.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 提现模块提现方式绑定相关接口控制器
 * </p>
 *
 * @author shenbh
 * @since 2023-03-30
 */
@Slf4j
@Api(tags = "提现模块提现方式-对公账号绑定相关接口")
@RestController
@RequestMapping("/eapp/v1.0/withdraw")
public class WithdrawBindPublicController {

    @Autowired
    private WithdrawService withdrawService;

    @ApiOperation("对公账号列表页面-已绑定对公账号列表接口")
    @PostMapping("/company/listBound")
    public Response<List<CardInfoDTO>> publicAccountListBound() {
        List<CardInfoDTO> dto = withdrawService.getEmployeeBinds(LoginUserContext.getUser().getId(), WithdrawCardTypeEnum.PUBLIC_ACCOUNT);
        desensitizeBankcard(dto);
        return Response.ok(dto);
    }

    public void desensitizeBankcard(List<CardInfoDTO> dtos) {
        if (CollectionUtil.isNotEmpty(dtos)) {
            for (CardInfoDTO cardInfoDTO : dtos) {
                cardInfoDTO.setAccountNo(subBankCardNo(DesensitizedUtil.bankCard(cardInfoDTO.getAccountNo())));
            }
        }

    }

    public static String subBankCardNo(String bankCard) {
        if (bankCard.length() <= 8) {
            return bankCard;
        }
        return bankCard.substring(bankCard.length() - 8, bankCard.length());
    }

    @ApiOperation("绑定对公账号页面-绑定对公账号接口")
    @PostMapping("/company/bind")
    public Response bindPublicAccount(@RequestBody @Validated @Valid PublicAccountBindReq req) {
        withdrawService.bindPublicAccount(LoginUserContext.getUser().getId(), req);
        return Response.ok();
    }

    @ApiOperation("已绑定对公账号详情页面-解绑对公账号接口")
    @PostMapping("/company/unbind")
    public Response unbindPublicAccount(@RequestBody @Validated CardIdReq req) {
        withdrawService.unbindBankcard(LoginUserContext.getUser().getId(), req.getCardId());
        return Response.ok();
    }

    @ApiOperation("已绑定对公账号详情页面-已绑定对公账号详情接口")
    @PostMapping("/company/getbindinfo")
    public Response<CardInfoDTO> getPublicAccountBindinfo(@RequestBody @Validated CardIdReq req) {
        CardInfoDTO dto = withdrawService.getWithdrawBindInfo(LoginUserContext.getUser().getId(), req.getCardId());
        return Response.ok(dto);
    }


}