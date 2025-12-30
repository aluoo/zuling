package com.anyi.sparrow.common.utils.channel;

import com.anyi.sparrow.common.utils.WxUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PwWxUtils extends WxUtils {
    @Value("${channel.pengwei.wx.appId:}")
    private String wxAppId;

    @Value("${channel.pengwei.wx.secret:}")
    private String wxSecret;

    @Override
    public String getWxAppId() {
        return wxAppId;
    }

    @Override
    public String getWxSecret() {
        return wxSecret;
    }
}
