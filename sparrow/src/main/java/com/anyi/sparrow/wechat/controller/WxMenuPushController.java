package com.anyi.sparrow.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 微信自定义菜单推送更新
 * </p>
 *
 * @author shenbh
 * @since 2023-09-14
 */
@Slf4j
@RestController
@Api(tags = "微信自定义菜单推送更新")
public class WxMenuPushController {

    @Autowired
    private WxMpService wxMpService;

    @ApiOperation("更新自定义菜单")
    @PostMapping("/wxMenuPush")
    public Map<String, Object> wxMenuPush(@RequestBody JSONObject menuJson) {

        log.info("【微信自定义菜单推送更新】:{}", menuJson);


        Map<String, Object> result = new HashMap<>();
        result.put("code", "1" ); //返回标志（0-失败 1-成功）
        FLagInfo fLagInfo = new FLagInfo();
        fLagInfo.setFlag( "0");//业务处理情况（0-成功 1-失败）
        result.put("content", fLagInfo);
        result.put("message",  "success" );
        return result;
    }



    @Data
    private static class FLagInfo {
        private String flag;
    }


}
