package com.anyi.sparrow.wechat.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemplateMsgService {


    @Autowired
    private WxMpService wxMpService;

    @Value("${spring.profiles.active}")
    private String env;


    private void send(WxMpTemplateMessage msg) throws WxErrorException {
//        if (SpringContextUtil.isProduction()) {
//            wxMpService.getTemplateMsgService().sendTemplateMsg(msg);
//        } else {
//            log.info("【公众号模版消息推送】: 非正式环境，模拟推送");
//        }
        wxMpService.getTemplateMsgService().sendTemplateMsg(msg);
    }

    /**
     * 产品名称
     * {{thing8.DATA}}
     * <p>
     * 订单编号
     * {{character_string9.DATA}}
     * <p>
     * 订单来源
     * {{phrase1.DATA}}
     * <p>
     * 下单时间
     * {{time2.DATA}}
     * <p>
     */
   /* @Async("aycx-executor")*/
    public void pushOrderMsg(String openId, String name, String orderNo, String shop, String date4, String orderId) {
        if (name.length() > 20) {
            name = StrUtil.sub(name, 0, 17) + "...";
        }
        String thing7Data = StrUtil.format("来自{}", shop);
        if (thing7Data.length() > 20) {
            thing7Data = StrUtil.sub(thing7Data, 0, 17) + "...";
        }
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                // .toUser("ooC4P61JFF1Enq9eyD76zeE2Xu-k")//要推送的用户openid
                .templateId("q2urIGTKo45bssMUyn0ntnKdNOSbHyzaWXedYbR8DYI")//模版id
                .toUser(openId)//要推送的用户openid
                //.url("https://30paotui.com/")//点击模版消息要访问的网址
                // .url(StrUtil.format("/pages/order/detail?id={}", orderId))
                .miniProgram(getMiniProgram(StrUtil.format("pages/order/detail?id={}", orderId)))
                .build()
                .addData(new WxMpTemplateData("thing8", name))
                .addData(new WxMpTemplateData("character_string9", orderNo))
                .addData(new WxMpTemplateData("thing7", thing7Data))
                .addData(new WxMpTemplateData("time2", date4));
        try {
            log.info("公众号模版消息推送创建订单{}", JSONUtil.toJsonStr(templateMessage));
            send(templateMessage);
        } catch (WxErrorException e) {
            throw new BusinessException(BizError.WX_MAP_TEMPLATE_ERROR);
        }
    }


    private WxMpTemplateMessage.MiniProgram getMiniProgram(String miniUrl) {
        return new WxMpTemplateMessage.MiniProgram("wxf715e0b173c6c9d4", miniUrl, true);
    }
}