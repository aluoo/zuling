package com.anyi.sparrow.applet.user.controller;

import com.anyi.sparrow.applet.user.service.UserAccountProcessService;
import com.anyi.sparrow.applet.user.vo.UpdateUserDTO;
import com.anyi.sparrow.applet.user.vo.UserInfoVO;
import com.anyi.common.domain.param.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author peng can
 * @date 2022/12/1
 */
@RestController
@RequestMapping("/applet/user")
@Api(tags = "小程序用户接口")
public class UserAccountController {

    @Autowired
    private UserAccountProcessService userAccountProcessService;

    @GetMapping("/v1.0/user-info")
    @ApiOperation("获取用户基本信息")
    public Response<UserInfoVO> getUserInfo() {

        UserInfoVO infoVO = userAccountProcessService.getUserInfo();
        return Response.ok(infoVO);
    }


    @PostMapping("/v1.0/update")
    @ApiOperation("更新用户昵称、头像")
    public Response<UserInfoVO> updateUser(@RequestBody UpdateUserDTO userDTO) {

        UserInfoVO infoVO = userAccountProcessService.updateUser(userDTO);
        return Response.ok(infoVO);
    }


}
