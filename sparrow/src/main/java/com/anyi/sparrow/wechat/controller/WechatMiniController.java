package com.anyi.sparrow.wechat.controller;

import com.anyi.common.aspect.WebLog;
import com.anyi.sparrow.applet.user.service.UserAccountProcessService;
import com.anyi.sparrow.applet.user.vo.PhoneNumberVO;
import com.anyi.sparrow.applet.user.vo.UserTokenVO;
import com.anyi.sparrow.organize.invite.service.SpreadCodeService;
import com.anyi.sparrow.organize.invite.vo.SpreadCodeVo;
import com.anyi.sparrow.wechat.vo.WechatLoginDTO;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序相关接口
 *
 * @author peng can
 * @date 2022/12/3 14:01
 */
@RestController
@RequestMapping("/wechat/mini")
@Api(tags = "小程序授权接口")
public class WechatMiniController {

    @Autowired
    private UserAccountProcessService userAccountProcessService;
    @Autowired
    private SpreadCodeService spreadCodeService;

    @ApiOperation("wx登录")
    @PostMapping("/v1.0/wx-login")
    public Response<UserTokenVO> wxLogin(@RequestBody @Validated WechatLoginDTO loginDTO) {

        UserTokenVO tokenVO = userAccountProcessService.loginByCode(loginDTO);
        return Response.ok(tokenVO);
    }

    @ApiOperation("wx绑定手机号码")
    @GetMapping("/v1.0/bind-phone")
    @WebLog(description = "wx绑定手机号码")
    public Response<PhoneNumberVO> bindPhoneNumber(@RequestParam String authCode) {
        PhoneNumberVO phoneNumberVO = userAccountProcessService.bindPhoneNumber(authCode);
        return Response.ok(phoneNumberVO);
    }

    @ApiOperation("服务商邀请员工二维码")
    @GetMapping("/inviteEmployee")
    @WebLog(description = "服务商邀请员工二维码")
    public Response<SpreadCodeVo> inviteEmployee() {
        return Response.ok(spreadCodeService.serviceEmployee());
    }

    @ApiOperation("手机回收端员工登录")
    @PostMapping("/mobile/wx-login")
    public Response<UserTokenVO> mobileLogin(@RequestParam String authCode) {
        UserTokenVO tokenVO = userAccountProcessService.mobileLogin(authCode);
        return Response.ok(tokenVO);
    }

}
