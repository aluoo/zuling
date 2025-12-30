package com.anyi.common.service;

import cn.hutool.json.JSONUtil;
import com.anyi.common.qfu.QfuProperties;
import com.anyi.common.qfu.QfuService;
import com.anyi.common.qfu.dto.QfuPaymentCheckResp;
import com.anyi.common.qfu.dto.QfuPaymentInvokeReq;
import com.anyi.common.qfu.dto.QfuPaymentInvokeResp;
import com.anyi.sparrow.SparrowApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/21
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class QfuServiceTest extends SparrowApplicationTest {
    @Autowired
    QfuService qfuService;
    @Autowired
    QfuProperties qfuProperties;

    @Test
    public void paymentCheckTest() {
        QfuPaymentCheckResp resp = qfuService.paymentCheck("aycs0009", 2);
        System.out.println(JSONUtil.toJsonStr(resp));
        log.info("code {}", resp.getCode());
        log.info("msg {}", resp.getMessage());
        log.info("order {}", resp.getOrder());
    }

    @Test
    public void paymentInvokeTest() {
        String order = "aycs0009";
        QfuPaymentInvokeReq req = QfuPaymentInvokeReq.builder()
                .order(order)
                // .account("6226000000000000")
                .account("13700000000")
                .value(1)
                .name("test")
                .identity("400000199701010000")
                .phone("13700000000")
                .remarks("测试")
                // .batch("batch")
                // .title("title")
                .build();
        QfuPaymentInvokeResp resp = qfuService.paymentInvoke(req);
        log.info(JSONUtil.toJsonStr(resp));
        log.info("code {}", resp.getCode());
        log.info("msg {}", resp.getMessage());
        log.info("order {}", resp.getOrder());
    }

}