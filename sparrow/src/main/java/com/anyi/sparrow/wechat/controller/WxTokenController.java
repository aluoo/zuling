package com.anyi.sparrow.wechat.controller;

import com.anyi.common.domain.param.Response;
import com.anyi.sparrow.wechat.service.TemplateMsgService;
import com.anyi.sparrow.wechat.service.WxTokenService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WxTokenController {
    private Logger logger = LoggerFactory.getLogger(WxTokenController.class);

    @Autowired
    private WxTokenService wxTokenService;
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    TemplateMsgService templateMsgService;

    @ApiOperation("获取小程序AccessToken")
    @ResponseBody
    @RequestMapping(value = "/wx/mini/getAccessToken", method = RequestMethod.GET)
    public Response<String> getMiniAccessToken() {
        return Response.ok(wxTokenService.getMiniAccessToken());
    }

    @ApiOperation("获取公众号AccessToken")
    @ResponseBody
    @RequestMapping(value = "/wx/gzh/getAccessToken", method = RequestMethod.GET)
    public Response<String> getGzhAccessToken() {
        return Response.ok(wxTokenService.getGzhAccessToken());
    }

    @ApiOperation("测试公众号推送")
    @ResponseBody
    @RequestMapping(value = "/wx/gzh/push", method = RequestMethod.GET)
    public Response test() {
        //templateMsgService.pushOrderMsg("ooC4P61JFF1Enq9eyD76zeE2Xu-k", "测试推送", "1111", "安逸测试手机门店", "2024年03月21日");
        return Response.ok();
    }

}