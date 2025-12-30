package com.anyi.sparrow.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/17
 */
@AllArgsConstructor
@Getter
public enum MsgTemplateEnum {
    WELCOME_ACTIVE("ETC激活通知", "恭喜您成功激活ETC，感谢您选择安逸出行。安逸出行平台致力于为广大车主朋友提供方便快捷、安全保密、高效稳定的ETC服务，为每位车主的高速通行保驾护航。", "ETC激活通知"),
    PAY_SUCCESS_A("ETC套餐办理通知", "您已支付通行保畅金{amount}元，成功开通安逸出行ETC服务。可享受全国高速出行（港澳台除外），先通行后付费、全国高速最低8.5折、微信代扣、设备质保{device_quality_months}年，随时免费注销（免费换绑其他车辆）等服务。", "ETC套餐办理通知A套餐"),
    PAY_SUCCESS_B("ETC套餐办理通知", "您已支付通行保畅金{amount}元，成功开通安逸出行ETC服务。可享受全国高速出行（港澳台除外），先通行后付费、全国高速最低8.5折、支持微信代扣、设备质保{device_quality_months}年、免费注销、保障用户高效通行微信余额不足平台先行垫付。满{contract_months}个使用月平台给予{amount}元通行奖励金（可用于通行抵扣）。", "ETC套餐办理通知B套餐"),
    PAY_SUCCESS_C1C2("ETC套餐办理通知", "您已支付通行保畅金{amount}元，成功开通安逸出行ETC服务。可享受全国高速出行（港澳台除外），先通行后付费、全国高速最低8.5折、微信代扣、设备质保{device_quality_months}年，微信余额不足时平台先行垫付，安逸出行保障您的高效通行。", "ETC套餐办理通知C1/C2套餐"),
    PAY_SUCCESS_C3C4("ETC套餐办理通知", "您已支付通行保畅金{amount}元，成功开通安逸出行ETC服务。可享受全国高速出行（港澳台除外），先通行后付费、全国高速最低8.5折、微信代扣、设备质保{device_quality_months}年，安逸出行保障您的高效通行。", "ETC套餐办理通知C3/C4套餐"),
    PAY_SUCCESS_D1D2("ETC套餐办理通知", "您已支付ETC设备工本费{amount}元，成功开通安逸出行ETC服务。可享受全国高速出行（港澳台除外），先通行后付费、全国高速最低9.5折、微信代扣、设备质保{device_quality_months}年，随时免费注销（免费换绑其他车辆）等服务。", "ETC套餐办理通知D1/D2套餐"),
    PAY_SUCCESS_X1X2("ETC套餐办理通知", "您已支付ETC会员费{amount}元，成功开通安逸出行ETC服务。可享受全国高速出行（港澳台除外），先通行后付费、全国高速最低9.5折、微信代扣、设备质保{device_quality_months}年，随时免费注销（免费换绑其他车辆）等服务。", "ETC套餐办理通知X1/X2套餐"),
    ;

    private final String title;
    private final String template;
    private final String desc;
}