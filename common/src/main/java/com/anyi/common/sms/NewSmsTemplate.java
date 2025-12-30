package com.anyi.common.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewSmsTemplate {
    /**
     * ETC激活 <br/>
     * 模板编号：253126   <br/>
     * 模板内容：【安逸出行】尊敬的客户，您的车辆(#car_code#)的ETC设备已成功激活！上高速先通行后付费，祝您一路顺风！<br/>
     * 触发节点:设备一次激活成功<br/>
     * 触发端：app管理端<br/>
     * 类型： 通知类
     */
    USER_ETC_ACTIVE("253126", "【安逸出行】尊敬的客户，您的车辆(#car_code#)的ETC设备已成功激活！上高速先通行后付费，祝您一路顺风！"),

    USER_ETC_ARREARAGE("252912", "【安逸出行】尊敬的客户，您的车辆(#car_code#)高速通行账单欠费超时未补缴，名下ETC卡均无法使用，请尽快处理。进入“安逸出行ETC”微信小程序查看详情！"),
    /**
     * 车主手机号验证“发送验证码”<br/>
     * 模板编号：252760 <br/>
     * 模板内容:【安逸出行】您正在进行身份认证，您的验证码是#code#。如非本人操作，请忽略本短信<br/>
     * 触发节点：车主手机号验证“发送验证码”<br/>
     * 触发端：app管理端<br/>
     * 类型： 验证码类
     */
    USER_ETC_ID_AUTH("252760", "【安逸出行】您正在进行身份认证，您的验证码是#code#。如非本人操作，请忽略本短信"),
    /**
     * 登录短信验证码 <br/>
     * 模板编号：252759 <br/>
     * 模板内容:【安逸管理版】您正在登录，您的验证码是#code#。如非本人操作，请忽略本短信<br/>
     * 触发节点：登录验证“发送验证码”<br/>
     * 触发端：app管理端<br/>
     * 类型： 验证码类
     */
    USER_ETC_LOGIN("262405", "【机享转】您正在登录，您的验证码是#code#。如非本人操作，请忽略本短信"),

    /**
     * 解绑微信车主服务代扣通知 <br/>
     * 模板编号：252758 <br/>
     * 模板内容:【安逸出行】您的车辆(#car_code#)解绑了微信免密支付，为了不影响您使用，请进入“安逸出行ETC”小程序，点击“我的ETC”重新签约。<br/>
     * 触发节点：获取用户代扣状态时而且是解约状态时触发<br/>
     * 触发端：小程序获取用户状态<br/>
     * 类型： 通知类
     */
    USER_ETC_NON_SECRET_PAY("252758", "【安逸出行】您的车辆(#car_code#)解绑了微信免密支付，为了不影响您使用，请进入“安逸出行ETC”小程序，点击“我的ETC”重新签约。"),
    USER_ETC_RECOVER("252757", "【安逸出行】尊敬的客户，您的车辆(#car_code#)欠费已补缴成功，高速会在48小时内为您解除通行限制！"),
//    USER_ETC_PAY_FAIL("252542", "【安逸出行】尊敬的客户，您的车辆(#car_code#)有高速通行账单扣款失败，请尽快处理。进入“安逸出行ETC”微信小程序查看详情！"),
    /**
     * #yyyy年mm月dd日#     #date#
     * #入口名称#     #enter_name#
     * #出口名称#      #exit_name#
     * #应扣金额#      #money#
     */
    USER_ETC_PAY_FAIL("252542", "【安逸出行】尊敬的客户，您的车辆(#car_code#)于#date#，在#enter_name#站驶入，至#exit_name#站使出，消费金额#money#元，微信扣费失败，请确保微信余额充足。或进入“安逸出行ETC”微信小程序进行支付，如有疑问请联系客服咨询！"),

    ADVANCE_NOTICE_FOR_IMPENDING_BLACK("255831", "【安逸出行】尊敬的(#car_code#)车主您好！您的车辆有#money#元高速通行费未支付，3小时后系统将自动关闭ETC通道。为避免影响您通行，请及时补缴，谢谢！"),
    ;
    private final String tplId;
    private final String name;


}