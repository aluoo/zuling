package com.anyi.sparrow.organize.invite.controller;

import com.anyi.common.advice.BusinessException;
import com.anyi.common.aspect.WebLog;
import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.invite.service.SpreadCodeService;
import com.anyi.sparrow.organize.invite.vo.SpreadCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Api(tags = "门店和服务商入驻二维码")
@RestController
@RequestMapping("/invite")
@Slf4j
public class SpreadInviteController {

    @Autowired
    SpreadCodeService spreadCodeService;

    @PostMapping("/company/create")
    @ApiOperation("生成邀请门店小程序二维码")
    @WebLog(description = "生成邀请门店小程序二维码")
    public Response<SpreadCodeVo> companyCreate() {
        return Response.ok(spreadCodeService.companyCreate());
    }

    @PostMapping("/recycle/create")
    @ApiOperation("生成邀请服务商小程序二维码")
    @WebLog(description = "生成邀请服务商小程序二维码")
    public Response<SpreadCodeVo> recycleCreate() {
        if(LoginUserContext.getUser().getLevel()!=0){
            throw new BusinessException(99999,"暂无权限");
        }
        return Response.ok(spreadCodeService.recycleCreate());
    }

    @PostMapping("/company/staff/create")
    @ApiOperation("生成邀请门店员工小程序二维码")
    @WebLog(description = "生成邀请门店员工小程序二维码")
    public Response<SpreadCodeVo> companyStaffCreate() {
        return Response.ok(spreadCodeService.companyStaffCreate());
    }
}