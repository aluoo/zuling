package com.anyi.sparrow.withdraw.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;

import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import com.anyi.sparrow.withdraw.dto.CardInfoDTO;
import com.anyi.sparrow.withdraw.req.CardIdReq;
import com.anyi.sparrow.withdraw.req.ZfbBindReq;
import com.anyi.sparrow.withdraw.service.WithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Api(tags = "提现模块提现方式-支付宝绑定相关接口")
@RestController
@RequestMapping("/eapp/v1.0/withdraw")
public class WithdrawBindZFBController {

    @Autowired
    private WithdrawService withdrawService;

    @ApiOperation("支付宝列表页面-已绑定支付宝列表接口")
    @PostMapping("/zfb/listBound")
    public Response<List<CardInfoDTO>> zfbListBound() {
        List<CardInfoDTO> dto = withdrawService.getEmployeeBinds(LoginUserContext.getUser().getId(), WithdrawCardTypeEnum.ZFB_ACCOUNT);
        desensitizeZfb(dto);
        return Response.ok(dto);
    }

    public void desensitizeZfb(List<CardInfoDTO> dtos) {
        if (CollectionUtil.isNotEmpty(dtos)) {
            for (CardInfoDTO cardInfoDTO : dtos) {
                if (StringUtils.contains("@", cardInfoDTO.getAccountNo())) {
                    cardInfoDTO.setAccountNo(DesensitizedUtil.email(cardInfoDTO.getAccountNo()));
                } else {
                    cardInfoDTO.setAccountNo(DesensitizedUtil.mobilePhone(cardInfoDTO.getAccountNo()));
                }
            }
        }

    }

    @ApiOperation("绑定支付宝页面-绑定支付宝接口")
    @PostMapping("/zfb/bind")
    public Response bindZfb(@RequestBody @Validated @Valid ZfbBindReq req) {
        withdrawService.bindZFB(LoginUserContext.getUser().getId(), req);
        return Response.ok();
    }

    @ApiOperation("已绑定支付宝详情页面-解绑支付宝接口")
    @PostMapping("/zfb/unbind")
    public Response unbindZfb(@RequestBody @Validated CardIdReq req) {
        withdrawService.unbindBankcard(LoginUserContext.getUser().getId(), req.getCardId());
        return Response.ok();
    }

    @ApiOperation("已绑定支付宝详情页面-已绑定支付宝详情接口")
    @PostMapping("/zfb/getbindinfo")
    public Response<CardInfoDTO> getZfbBindinfo(@RequestBody @Validated CardIdReq req) {
        CardInfoDTO dto = withdrawService.getWithdrawBindInfo(LoginUserContext.getUser().getId(), req.getCardId());
        return Response.ok(dto);
    }


}
