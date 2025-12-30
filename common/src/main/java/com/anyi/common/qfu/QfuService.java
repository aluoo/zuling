package com.anyi.common.qfu;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.qfu.dto.QfuPaymentCheckResp;
import com.anyi.common.qfu.dto.QfuPaymentInvokeReq;
import com.anyi.common.qfu.dto.QfuPaymentInvokeResp;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class QfuService {
    @Autowired
    QfuApi qfuApi;
    @Autowired
    QfuApi qfuAlipayApi;
    @Autowired
    QfuProperties qfuProperties;
    @Autowired
    QfuAlipayProperties afuAlipayProperties;

    // 查询结果
    public QfuPaymentCheckResp paymentCheck(String order, Integer type) {
        Map<String, Object> resp;
        try {
            if (type != null && type.equals(2)) {
                // 支付宝
                resp = qfuAlipayApi.paymentCheck(order);
            } else {
                // 银行卡
                resp = qfuApi.paymentCheck(order);
            }
            // resp = qfuApi.paymentCheck(order);
            String json = new Gson().toJson(resp);
            return JSONUtil.toBean(json, QfuPaymentCheckResp.class);
        } catch (Exception e) {
            log.error("payment check error - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }

    // 单笔付款
    public QfuPaymentInvokeResp paymentInvoke(QfuPaymentInvokeReq req) {
        Map<String, Object> resp;
        try {
            if (req.getType() != null && req.getType().equals(2)) {
                // 支付宝
                resp = qfuAlipayApi.paymentInvoke(afuAlipayProperties.getMerchantId(), req.getOrder(), req.getAccount(), req.getValue(), req.getName(), req.getIdentity(), req.getPhone(), req.getRemarks(), req.getBatch(), req.getTitle());
            } else {
                // 银行卡
                resp = qfuApi.paymentInvoke(qfuProperties.getMerchantId(), req.getOrder(), req.getAccount(), req.getValue(), req.getName(), req.getIdentity(), req.getPhone(), req.getRemarks(), req.getBatch(), req.getTitle());
            }
            // resp = qfuApi.paymentInvoke(qfuProperties.getMerchantId(), req.getOrder(), req.getAccount(), req.getValue(), req.getName(), req.getIdentity(), req.getPhone(), req.getRemarks(), req.getBatch(), req.getTitle());
            String json = new Gson().toJson(resp);
            return JSONUtil.toBean(json, QfuPaymentInvokeResp.class);
        } catch (Exception e) {
            log.error("payment invoke error - {}", ExceptionUtil.getMessage(e));
        }
        return null;
    }
}