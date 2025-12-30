package com.anyi.sparrow.wechat.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.json.JSONUtil;
import com.anyi.common.util.SpringContextUtil;
import com.anyi.sparrow.wechat.enums.SubscribeMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SubscribeMsgService {


    @Autowired
    private WxMaService wxMaService;

    @Value("${spring.profiles.active}")
    private String env;

    //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
    private static final String MINI_PROGRAM_STATE = "trial";

    /**
     * 获取小程序跳转类型
     * @return 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    public String getToMiniAppType(){
        if ("production".equals(env)){
            return "formal";
        }

        if ("test".equals(env)){
            return "trial";
        }
        return "developer";
    }

    private void send(WxMaSubscribeMessage msg) throws WxErrorException {
        if (SpringContextUtil.isProduction()) {
            wxMaService.getMsgService().sendSubscribeMsg(msg);
        } else {
            log.info("【小程序消息推送】: 非正式环境，模拟推送");
        }
    }

    /**
     * 退款原因
     * {{thing1.DATA}}
     * <p>
     * 车牌号
     * {{car_number2.DATA}}
     * <p>
     * 退款金额
     * {{amount3.DATA}}
     * <p>
     * 支付时间
     * {{date4.DATA}}
     * <p>
     * 退款结果
     * {{thing5.DATA}}
     */
    @Async
    public void pushRefundMsg(String openId, String thing1, String car_number2, String amount3, String date4, String thing5) {

        WxMaSubscribeMessage msg = WxMaSubscribeMessage.builder()
                .toUser(openId) // 小程序中用户的openId即可
                .templateId(SubscribeMsgTemplate.REFUND.getId()) // 服务号中的模板消息ID,不能是订阅消息ID
                .page("https://www.anyichuxing.com") // 点击消息,从微信中跳转的页面链接. 和 miniProgram 互斥,只能选一个
                .miniprogramState(getToMiniAppType()) //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                .build()
                .addData(new WxMaSubscribeMessage.MsgData("thing1", thing1))
                .addData(new WxMaSubscribeMessage.MsgData("car_number2", car_number2)) // 模板消息参数, 参数名不要带.DATA
                .addData(new WxMaSubscribeMessage.MsgData("amount3", amount3))
                .addData(new WxMaSubscribeMessage.MsgData("date4", date4))
                .addData(new WxMaSubscribeMessage.MsgData("thing5", thing5));
        try {
            log.info("【小程序消息推送】退款原因 - {}", JSONUtil.toJsonStr(msg));
            send(msg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 2. ETC服务状态提醒
     * <p>
     * 车牌号
     * {{car_number1.DATA}}
     * <p>
     * 当前状态
     * {{thing2.DATA}}
     * <p>
     * 起始时间
     * {{date3.DATA}}
     * <p>
     * 说明
     * {{thing4.DATA}}
     */
    @Async
    public void pushStatusMsg(String openId, String car_number1, String thing2, String date3, String thing4) {

        WxMaSubscribeMessage msg = WxMaSubscribeMessage.builder()
                .toUser(openId) // 小程序中用户的openId即可
                .templateId(SubscribeMsgTemplate.STATUS.getId()) // 服务号中的模板消息ID,不能是订阅消息ID
                .page("/pages/travel/index?plateNumber=" + car_number1) // 点击消息,从微信中跳转的页面链接. 和 miniProgram 互斥,只能选一个
                .miniprogramState(getToMiniAppType()) //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                .build()
                .addData(new WxMaSubscribeMessage.MsgData("car_number1", car_number1))
                .addData(new WxMaSubscribeMessage.MsgData("thing2", thing2)) // 模板消息参数, 参数名不要带.DATA
                .addData(new WxMaSubscribeMessage.MsgData("date3", date3))
                .addData(new WxMaSubscribeMessage.MsgData("thing4", thing4));
        try {
            log.info("【小程序消息推送】ETC服务状态提醒-拉黑通知 - {}", JSONUtil.toJsonStr(msg));
            //wxMaService.getMsgService().sendSubscribeMsg(msg);
            send(msg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 6. ETC服务状态提醒-解黑通知
     * <p>
     * 车牌号
     * {{car_number2.DATA}}
     * <p>
     * 当前状态
     * {{thing2.DATA}}
     * <p>
     * 起始时间
     * {{date3.DATA}}
     * <p>
     * 说明
     * {{thing4.DATA}}
     */
    @Async
    public void pushStatusMsgWhite(String openId, String car_number1, String date3) {

        WxMaSubscribeMessage msg = WxMaSubscribeMessage.builder()
                .toUser(openId) // 小程序中用户的openId即可
                .templateId(SubscribeMsgTemplate.STATUS.getId()) // 服务号中的模板消息ID,不能是订阅消息ID
                .page("/pages/travel/index?plateNumber=" + car_number1) // 点击消息,从微信中跳转的页面链接. 和 miniProgram 互斥,只能选一个
                .miniprogramState(getToMiniAppType()) //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                .build()
                .addData(new WxMaSubscribeMessage.MsgData("car_number1", car_number1))
                .addData(new WxMaSubscribeMessage.MsgData("thing2", "已解除黑名单，ETC即将恢复使用")) // 模板消息参数, 参数名不要带.DATA
                .addData(new WxMaSubscribeMessage.MsgData("date3", date3))
                .addData(new WxMaSubscribeMessage.MsgData("thing4", "高速将在48小时内为您解除通行限制"));
        try {
            log.info("【小程序消息推送】ETC服务状态提醒-解黑通知 - {}", JSONUtil.toJsonStr(msg));
            //wxMaService.getMsgService().sendSubscribeMsg(msg);
            send(msg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 3、ETC还款成功通知
     * <p>
     * 车牌号
     * {{car_number1.DATA}}
     * <p>
     * 通行费
     * {{amount2.DATA}}
     * <p>
     * 支付方式
     * {{thing4.DATA}}
     * <p>
     * 支付时间
     * {{date3.DATA}}
     * <p>
     * 说明
     * {{thing5.DATA}}
     */
    @Async
    public void pushRepayMsg(String openId, String car_number1, String amount2, String date3, String thing4, String thing5) {

        WxMaSubscribeMessage msg = WxMaSubscribeMessage.builder()
                .toUser(openId) // 小程序中用户的openId即可
                .templateId(SubscribeMsgTemplate.REPAY.getId()) // 服务号中的模板消息ID,不能是订阅消息ID
                .page("/pages/index/index?plateNumber=" + car_number1) // 点击消息,从微信中跳转的页面链接. 和 miniProgram 互斥,只能选一个
                .miniprogramState(getToMiniAppType()) //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                .build()
                .addData(new WxMaSubscribeMessage.MsgData("car_number1", car_number1)) // 模板消息参数, 参数名不要带.DATA
                .addData(new WxMaSubscribeMessage.MsgData("amount2", amount2))
                .addData(new WxMaSubscribeMessage.MsgData("date3", date3))
                .addData(new WxMaSubscribeMessage.MsgData("thing4", thing4))
                .addData(new WxMaSubscribeMessage.MsgData("thing5", thing5));
        try {
            log.info("【小程序消息推送】ETC还款成功通知 - {}", JSONUtil.toJsonStr(msg));
            //wxMaService.getMsgService().sendSubscribeMsg(msg);
            send(msg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 4、ETC欠费提醒
     * <p>
     * 车牌号
     * {{car_number1.DATA}}
     * <p>
     * 欠费金额
     * {{amount2.DATA}}
     * <p>
     * 补缴时限
     * {{date3.DATA}}
     * <p>
     * 欠费说明
     * {{thing4.DATA}}
     */
    @Async
    public void pushArrearageMsg(String openId, Long tripOrderId,String car_number1, String amount2, String date3, String thing4) {

        WxMaSubscribeMessage msg = WxMaSubscribeMessage.builder()
                .toUser(openId) // 小程序中用户的openId即可
                .templateId(SubscribeMsgTemplate.ARREARAGE.getId()) // 服务号中的模板消息ID,不能是订阅消息ID
                .page(" /pages/travel/payback?plateNumber=" + car_number1 +"&orderId="+tripOrderId) // 点击消息,从微信中跳转的页面链接. 和 miniProgram 互斥,只能选一个
                .miniprogramState(getToMiniAppType()) //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                .build()
                .addData(new WxMaSubscribeMessage.MsgData("car_number1", car_number1)) // 模板消息参数, 参数名不要带.DATA
                .addData(new WxMaSubscribeMessage.MsgData("amount2", amount2))
                .addData(new WxMaSubscribeMessage.MsgData("date3", date3))
                .addData(new WxMaSubscribeMessage.MsgData("thing4", thing4));
        try {
            log.info("【小程序消息推送】ETC欠费提醒 - {}", JSONUtil.toJsonStr(msg));
            //wxMaService.getMsgService().sendSubscribeMsg(msg);
            send(msg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 5. 高速扣费通知
     * <p>
     * 通行时间
     * {{time1.DATA}}
     * <p>
     * 出入口站点
     * {{thing2.DATA}}
     * <p>
     * 车牌号
     * {{car_number3.DATA}}
     * <p>
     * 消费金额
     * {{amount12.DATA}}
     * <p>
     * 备注说明
     * {{thing11.DATA}}
     */
    @Async
    public void pushDeductMsg(String openId, String time1, String thing2, String car_number3, String amount12, String thing11) {

        WxMaSubscribeMessage msg = WxMaSubscribeMessage.builder()
                .toUser(openId) // 小程序中用户的openId即可
                .templateId(SubscribeMsgTemplate.DEDUCT.getId()) // 服务号中的模板消息ID,不能是订阅消息ID
                .page("/pages/travel/index?plateNumber=" + car_number3) // 点击消息,从微信中跳转的页面链接. 和 miniProgram 互斥,只能选一个
                .miniprogramState(getToMiniAppType()) //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
                .build()
                .addData(new WxMaSubscribeMessage.MsgData("time1", time1)) // 模板消息参数, 参数名不要带.DATA
                .addData(new WxMaSubscribeMessage.MsgData("thing2", thing2))
                .addData(new WxMaSubscribeMessage.MsgData("car_number3", car_number3))
                .addData(new WxMaSubscribeMessage.MsgData("amount12", amount12))
                .addData(new WxMaSubscribeMessage.MsgData("thing11", thing11));
        try {
            log.info("【小程序消息推送】高速扣费通知 - {}", JSONUtil.toJsonStr(msg));
            //wxMaService.getMsgService().sendSubscribeMsg(msg);
            send(msg);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}