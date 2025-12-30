package com.anyi.sparrow.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscribeMsgTemplate {

    REFUND("iuKlTG9LLZJ85rgiHEtBPPeZmCLra4rDgkcT0p61Tm4", "4800", "ETC退款通知"),
    STATUS("_h14SkHp-_bm_OxBz26tcW_478kiInjvbXPiu9EHLTU", "5882", "ETC服务状态提醒"),
    REPAY("NeZt5LCWhafYqqHVUcokQpHLSZMiwzmwNndsY204FXc", "4797", "ETC还款成功通知"),
    ARREARAGE("CRbUM41mBY93O1IG15KMj-mBm19_IJ-p7pi5cVI-9is", "5433", "ETC欠费提醒"),
    DEDUCT("73LHIEazp5YN9c2dEYIJG-6Vx6OceF4LMeI96OYPMcs", "136", "高速扣费通知"),
    ;
    private final String id;
    private final String code;
    private final String title;
}
