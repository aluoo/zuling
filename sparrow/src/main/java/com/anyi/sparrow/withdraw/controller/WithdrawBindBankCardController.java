package com.anyi.sparrow.withdraw.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import com.anyi.sparrow.withdraw.dto.CardInfoDTO;
import com.anyi.sparrow.withdraw.req.BandCardBindReq;
import com.anyi.sparrow.withdraw.req.CardIdReq;
import com.anyi.sparrow.withdraw.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 提现模块提现方式绑定相关接口控制器
 * </p>
 *
 * @author shenbh
 * @since 2023-03-30
 */
@Slf4j
@Api(tags = "提现模块提现方式绑定相关接口")
@RestController
@RequestMapping("/eapp/v1.0/withdraw")
public class WithdrawBindBankCardController {

    @Autowired
    private WithdrawService withdrawService;

    @ApiOperation("银行卡列表页面-已绑定银行卡列表接口")
    @PostMapping("/bankcard/listBound")
    public Response<List<CardInfoDTO>> bankcardListBound() {
        List<CardInfoDTO> dto = withdrawService.getEmployeeBinds(LoginUserContext.getUser().getId(), WithdrawCardTypeEnum.BANK_CARD);
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

    @ApiOperation("绑定银行卡页面-可选银行列表接口")
    @PostMapping("/bank/listForSelect")
    public Response<List> listBankForSelect() {

        List<String> bankList = getBankList();

        List<Map<String, String>> list = bankList.stream().map(item -> {
            Map<String, String> mapItem = new HashMap<>();
            mapItem.put("key", item);
            mapItem.put("value", item);
            return mapItem;
        }).collect(Collectors.toList());

        return Response.ok(list);
    }

    @NotNull
    private List<String> getBankList() {
        List<String> bankList = Arrays.asList(
                "中国农业银行"
                , "中国工商银行"
                , "中国银行"
                , "中国建设银行"
                , "中国民生银行"
                , "华夏银行"
                , "中国光大银行"
                , "中信实业银行"
                , "恒丰银行"
                , "上海浦东发展银行"
                , "交通银行"
                , "兴业银行"
                , "深圳发展银行"
                , "招商银行"
                , "广东发展银行"
                , "中国邮政储蓄"
        );
        return bankList;
    }

    @ApiOperation("绑定银行卡页面-绑定银行卡接口")
    @PostMapping("/bankcard/bind")
    public Response bindBankcard(@RequestBody @Validated @Valid BandCardBindReq req) {

        withdrawService.bindBankcard(LoginUserContext.getUser().getId(), req);

        return Response.ok();
    }

    @ApiOperation("已绑定银行卡详情页面-解绑银行卡接口")
    @PostMapping("/bankcard/unbind")
    public Response unbindBankcard(@RequestBody @Validated CardIdReq req) {

        withdrawService.unbindBankcard(LoginUserContext.getUser().getId(), req.getCardId());

        return Response.ok();
    }

    @ApiOperation("已绑定银行卡详情页面-已绑定银行卡详情接口")
    @PostMapping("/bankcard/getbindinfo")
    public Response<CardInfoDTO> getBankcardBindinfo(@RequestBody @Validated CardIdReq req) {

        CardInfoDTO dto = withdrawService.getWithdrawBindInfo(LoginUserContext.getUser().getId(), req.getCardId());

        return Response.ok(dto);
    }


}