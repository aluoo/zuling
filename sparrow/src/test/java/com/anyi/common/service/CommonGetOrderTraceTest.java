package com.anyi.common.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.jdl.JdlService;
import com.anyi.common.product.domain.dto.AddressDTO;
import com.anyi.common.product.domain.dto.LogisticsTraceDTO;
import com.anyi.common.product.domain.request.CreateLogisticsReq;
import com.anyi.sparrow.SparrowApplicationTest;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.CommonCreateOrderResponse;
import com.lop.open.api.sdk.domain.ECAP.CommonModifyCancelOrderApi.commonCancelOrderV1.CommonModifyCancelOrderResponse;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetActualFeeInfoV1.CommonActualFeeResponse;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetOrderInfoV1.CommonOrderInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/25
 * @Copyright
 * @Version 1.0
 */
@Slf4j
public class CommonGetOrderTraceTest extends SparrowApplicationTest {
    @Autowired
    JdlService service;

    @Test
    public void getOrderTrace() {
        List<LogisticsTraceDTO> list = service.getOrderTrace("JDVE00109839125");
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    @Ignore
    public void createOrder() {
        AddressDTO sender = AddressDTO.builder().contact("发送者").address("福建省,厦门市,集美区").detail("测试地址").phone("13112341234").build();
        AddressDTO receiver = AddressDTO.builder().contact("接收").address("重庆,重庆市,万州区").detail("测试地址").phone("13112341234").build();
        CreateLogisticsReq req = CreateLogisticsReq.builder()
                .sendAddress(sender)
                .receiveAddress(receiver)
                .pickupStartTime(DateUtil.parse("2024-03-26 11:00:00"))
                .pickupEndTime(DateUtil.parse("2024-03-26 12:00:00"))
                .build();
        CommonCreateOrderResponse resp = service.createOrder(req);
        System.out.println(JSONUtil.toJsonStr(resp));
    }

    @Test
    public void getActualFeeInfo() {
        // CommonActualFeeResponse resp = service.getActualFeeInfo("JDVE00109839125");
        CommonActualFeeResponse resp = service.getActualFeeInfo("JDVE00109897560");
        System.out.println(JSONUtil.toJsonStr(resp));
    }

    @Test
    public void cancelOrder() {
        CommonModifyCancelOrderResponse data = service.cancelOrder("JDVE00109839125");
        System.out.println(JSONUtil.toJsonStr(data));
    }

    @Test
    public void subscribeTrace() {
        Boolean resp = service.subscribeTrace("JDVE1234", "17605968421");
        System.out.println(resp);
    }

    @Test
    public void getOrderInfo() {
        CommonOrderInfoResponse resp = service.getOrderInfo("JDVE00109897560");
        System.out.println(JSONUtil.toJsonStr(resp));
    }
}