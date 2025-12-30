package com.anyi.sparrow.assist.verify.enums;

import com.anyi.common.sms.NewSmsTemplate;

public enum BizEnum {

//    OPEN_ETC("open-etc", "SMS_171745980"),
    /**
     * 业务类型:车主手机号验证“发送验证码”
     */
    OPEN_ETC("open-etc", NewSmsTemplate.USER_ETC_ID_AUTH.getTplId()),
//    EM_OBU("em-obu", "SMS_171751005"),
//    BIND_MOBILE("bind-mobile", "SMS_171745983"),
    /**
     * 业务类型:员工登录验证码
     */
    EM_LOGIN("em-login", NewSmsTemplate.USER_ETC_LOGIN.getTplId()),
//    EM_Register("em-register", "SMS_167180391"),
//    QL_SIGN("ql_sign", "SMS_175400635"),
//    EM_TX_BANK("em-tx-bank","SMS_175536873"),
    ;
    private String biz;

    private String templateCode;

    BizEnum(String biz, String templateCode) {
        this.biz = biz;
        this.templateCode = templateCode;
    }

    public String getBiz() {
        return biz;
    }

    public String getTemplateCode() {
        return templateCode;
    }
}
