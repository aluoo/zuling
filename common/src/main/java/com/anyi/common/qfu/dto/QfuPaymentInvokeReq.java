package com.anyi.common.qfu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QfuPaymentInvokeReq implements Serializable {
    private static final long serialVersionUID = 1L;

    //建议用队列控制，保持 5 个并发以内。
    // 企业支付宝渠道，account填支付宝邮箱号或支付宝手机号。
    // 默认为直发模式，发起即刻打款；也可配置为挂起模式，此模式需要到 WEB 控制台终审。

    // 业务ID（注意唯一）
    private String order;

    // 银行卡号
    private String account;

    // 金额（分钱）
    private Integer value;

    // 姓名
    private String name;

    // 身份证号
    private String identity;

    // 手机号码
    private String phone;

    // 备注
    private String remarks;

    // 批次编号 2022-01-01
    private String batch;

    // 客户抬头 客户公司名称（以便生成自由职业者协议）
    private String title;

    // 类型(1-银行卡、2-支付宝、3-对公账户)
    private Integer type;
}