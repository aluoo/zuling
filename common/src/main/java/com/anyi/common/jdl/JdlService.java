package com.anyi.common.jdl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BaseException;
import com.anyi.common.jdl.config.JdlProperties;
import com.anyi.common.jdl.enums.OrderOriginEnum;
import com.anyi.common.jdl.exception.JdlBizException;
import com.anyi.common.product.domain.constants.TreeConstants;
import com.anyi.common.product.domain.dto.AddressDTO;
import com.anyi.common.product.domain.dto.LogisticsTraceDTO;
import com.anyi.common.product.domain.request.CreateLogisticsReq;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.*;
import com.lop.open.api.sdk.domain.ECAP.CommonModifyCancelOrderApi.commonCancelOrderV1.CommonModifyCancelOrderResponse;
import com.lop.open.api.sdk.domain.ECAP.CommonModifyCancelOrderApi.commonCancelOrderV1.CommonOrderCancelRequest;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetActualFeeInfoV1.CommonActualFeeRequest;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetActualFeeInfoV1.CommonActualFeeResponse;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetActualFeeInfoV1.Response;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetOrderInfoV1.CommonOrderInfoRequest;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetOrderInfoV1.CommonOrderInfoResponse;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetOrderTraceV1.CommonOrderTraceDetail;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetOrderTraceV1.CommonOrderTraceRequest;
import com.lop.open.api.sdk.domain.ECAP.CommonSubscribeTraceApi.commonSubscribeTraceV1.CommonSubscribeTraceRequest;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.request.ECAP.*;
import com.lop.open.api.sdk.response.ECAP.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/26
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class JdlService {
    @Autowired
    private JdlProperties jdlProperties;
    @Autowired
    private DefaultDomainApiClient jdlDomainApiClient;
    @Autowired
    private LopPlugin produceLopPlugin;
    @Autowired
    private SnowflakeIdService snowflakeIdService;

    /**
     * 下单接口 (commonCreateOrderV1)
     * @link <a href="https://cloud.jdl.com/#/open-business-document/api-doc/267/841">下单接口 (commonCreateOrderV1)</a>
     */
    public CommonCreateOrderResponse createOrder(CreateLogisticsReq req) {
        CommonCreateOrderRequest commonRequest = new CommonCreateOrderRequest();
        commonRequest.setOrderOrigin(OrderOriginEnum.B2C.getCode());
        commonRequest.setCustomerCode(jdlProperties.getCustomerCode());
        commonRequest.setSettleType(3);
        commonRequest.setProductsReq(buildProductInfo());
        commonRequest.setCargoes(Collections.singletonList(buildCargoInfo()));

        commonRequest.setOrderId(snowflakeIdService.nextId().toString());
        commonRequest.setSenderContact(buildContact(req.getSendAddress()));
        commonRequest.setReceiverContact(buildContact(req.getReceiveAddress()));
        commonRequest.setPickupStartTime(req.getPickupStartTime());
        commonRequest.setPickupEndTime(req.getPickupEndTime());

        // build req
        EcapV1OrdersCreateLopRequest request = new EcapV1OrdersCreateLopRequest();
        request.addLopPlugin(produceLopPlugin);
        request.setRequest(commonRequest);
        try {
            log.info("JDL.createOrder.info: request-{}", JSONUtil.toJsonStr(request));
            EcapV1OrdersCreateLopResponse response = jdlDomainApiClient.execute(request);
            log.info("JDL.createOrder.info: response-{}", JSONUtil.toJsonStr(response));
            if (!response.getResult().getSuccess()) {
                throw new BaseException(-1, response.getResult().getMsg());
            }
            return response.getResult().getData();
        } catch (LopException | BaseException e) {
            log.error("JDL.createOrder.error: {}", ExceptionUtil.getMessage(e));
            throw new JdlBizException(-1, e.getMessage());
        }
    }

    /**
     * 查询运单全程跟踪 (commonGetOrderTraceV1)
     * @link <a href="https://cloud.jdl.com/#/open-business-document/api-doc/267/1039"/>查询运单全程跟踪 (commonGetOrderTraceV1)</a>
     */
    public List<LogisticsTraceDTO> getOrderTrace(String waybillCode) {
        CommonOrderTraceRequest commonRequest = new CommonOrderTraceRequest();
        commonRequest.setOrderOrigin(OrderOriginEnum.B2C.getCode());
        commonRequest.setCustomerCode(jdlProperties.getCustomerCode());
        commonRequest.setWaybillCode(waybillCode);

        EcapV1OrdersTraceQueryLopRequest request = new EcapV1OrdersTraceQueryLopRequest();
        request.addLopPlugin(produceLopPlugin);
        request.setCommonOrderTraceRequest(commonRequest);

        try {
            log.info("JDL.getOrderTrace.info: request-{}", JSONUtil.toJsonStr(request));
            EcapV1OrdersTraceQueryLopResponse response = jdlDomainApiClient.execute(request);
            log.info("JDL.getOrderTrace.info: response-{}", JSONUtil.toJsonStr(response));
            if (!response.getResult().getSuccess()) {
                log.error("JDL.getOrderTrace.error: {}", response.getResult().getMsg());
                return null;
            }
            List<CommonOrderTraceDetail> details = response.getResult().getData().getTraceDetails();
            return BeanUtil.copyToList(details, LogisticsTraceDTO.class);
        } catch (LopException e) {
            log.error("JDL.getOrderTrace.error: {}", ExceptionUtil.getMessage(e));
            throw new JdlBizException(-1, e.getMessage());
        }
    }

    /**
     * 实际费用查询 (commonGetActualFeeInfoV1)
     * @link <a href="https://cloud.jdl.com/#/open-business-document/api-doc/267/843">实际费用查询 (commonGetActualFeeInfoV1)</a>
     */
    public CommonActualFeeResponse getActualFeeInfo(String waybillCode) {
        CommonActualFeeRequest commonRequest = new CommonActualFeeRequest();
        commonRequest.setOrderOrigin(OrderOriginEnum.B2C.getCode());
        commonRequest.setCustomerCode(jdlProperties.getCustomerCode());
        commonRequest.setWaybillCode(waybillCode);

        EcapV1OrdersActualfeeQueryLopRequest request = new EcapV1OrdersActualfeeQueryLopRequest();
        request.addLopPlugin(produceLopPlugin);
        request.setRequest(commonRequest);

        try {
            log.info("JDL.getActualFeeInfo.info: request-{}", JSONUtil.toJsonStr(request));
            EcapV1OrdersActualfeeQueryLopResponse response = jdlDomainApiClient.execute(request);
            log.info("JDL.getActualFeeInfo.info: response-{}", JSONUtil.toJsonStr(response));
            Response<CommonActualFeeResponse> result = response.getResult();
            if (!result.getSuccess()) {
                throw new BaseException(-1, response.getResult().getMsg());
            }
            return result.getData();
        } catch (LopException | BaseException e) {
            log.error("JDL.getActualFeeInfo.error: {}", ExceptionUtil.getMessage(e));
            throw new JdlBizException(-1, e.getMessage());
        }
    }

    /**
     * 订单取消 (commonCancelOrderV1)
     * @link <a href="https://cloud.jdl.com/#/open-business-document/api-doc/267/846">订单取消 (commonCancelOrderV1)</a>
     */
    public CommonModifyCancelOrderResponse cancelOrder(String waybillCode) {
        CommonOrderCancelRequest commonRequest = new CommonOrderCancelRequest();
        commonRequest.setOrderOrigin(OrderOriginEnum.B2C.getCode());
        commonRequest.setCancelReason("取消");
        commonRequest.setCancelReasonCode("1");
        commonRequest.setCustomerCode(jdlProperties.getCustomerCode());
        commonRequest.setWaybillCode(waybillCode);
        EcapV1OrdersCancelLopRequest request = new EcapV1OrdersCancelLopRequest();
        request.addLopPlugin(produceLopPlugin);
        request.setRequest(commonRequest);
        try {
            log.info("JDL.cancelOrder.info: request-{}", JSONUtil.toJsonStr(request));
            EcapV1OrdersCancelLopResponse response = jdlDomainApiClient.execute(request);
            log.info("JDL.cancelOrder.info: response-{}", JSONUtil.toJsonStr(response));
            if (!response.getResult().getSuccess()) {
                throw new BaseException(-1, response.getResult().getMsg());
            }
            return response.getResult().getData();
        } catch (LopException | BaseException e) {
            log.error("JDL.cancelOrder.error: {}", ExceptionUtil.getMessage(e));
            throw new JdlBizException(-1, e.getMessage());
        }
    }

    /**
     * 物流轨迹订阅 (commonSubscribeTraceV1)
     * @link <a href="https://cloud.jdl.com/#/open-business-document/api-doc/267/1038">物流轨迹订阅(commonSubscribeTraceV1)</a>
     */
    public Boolean subscribeTrace(String waybillCode, String mobile) {
        CommonSubscribeTraceRequest commonRequest = new CommonSubscribeTraceRequest();
        commonRequest.setCustomerCode(jdlProperties.getCustomerCode());
        commonRequest.setOrderOrigin(OrderOriginEnum.B2C.getCode());
        commonRequest.setWaybillCode(waybillCode);
        commonRequest.setMobile(PhoneUtil.subAfter(mobile).toString());

        EcapV1OrdersTraceSubscribeLopRequest request = new EcapV1OrdersTraceSubscribeLopRequest();
        request.addLopPlugin(produceLopPlugin);
        request.setRequest(commonRequest);
        try {
            log.info("JDL.subscribeTrace.info: request-{}", JSONUtil.toJsonStr(request));
            EcapV1OrdersTraceSubscribeLopResponse response = jdlDomainApiClient.execute(request);
            log.info("JDL.subscribeTrace.info: response-{}", JSONUtil.toJsonStr(response));
            if (!response.getResult().getSuccess()) {
                throw new JdlBizException(-1, response.getResult().getMsg());
            }
            return true;
        } catch (LopException | BaseException e) {
            log.error("JDL.subscribeTrace.error: {}", ExceptionUtil.getMessage(e));
            return false;
        }
    }

    public CommonOrderInfoResponse getOrderInfo(String waybillCode) {
        CommonOrderInfoRequest commonRequest = new CommonOrderInfoRequest();
        commonRequest.setOrderOrigin(OrderOriginEnum.B2C.getCode());
        commonRequest.setCustomerCode(jdlProperties.getCustomerCode());
        commonRequest.setWaybillCode(waybillCode);

        EcapV1OrdersDetailsQueryLopRequest request = new EcapV1OrdersDetailsQueryLopRequest();
        request.addLopPlugin(produceLopPlugin);
        request.setRequest(commonRequest);
        try {
            log.info("JDL.getOrderInfo.info: request-{}", JSONUtil.toJsonStr(request));
            EcapV1OrdersDetailsQueryLopResponse response = jdlDomainApiClient.execute(request);
            log.info("JDL.getOrderInfo.info: response-{}", JSONUtil.toJsonStr(response));
            if (!response.getResult().getSuccess()) {
                throw new JdlBizException(-1, response.getResult().getMsg());
            }
            return response.getResult().getData();
        } catch (LopException | BaseException e) {
            log.error("JDL.getOrderInfo.error: {}", ExceptionUtil.getMessage(e));
            throw new JdlBizException(-1, e.getMessage());
        }
    }

    private Contact buildContact(AddressDTO addr) {
        if (addr == null) {
            return null;
        }
        List<String> split = StrUtil.split(addr.getAddress(), TreeConstants.SPLIT_CHAR);
        StringBuilder prefix = new StringBuilder();
        split.forEach(prefix::append);
        String fullAddress = prefix + addr.getDetail();
        Contact c = new Contact();
        c.setName(addr.getContact());
        c.setMobile(addr.getPhone());
        c.setFullAddress(fullAddress);
        return c;
    }

    private CommonProductInfo buildProductInfo() {
        CommonProductInfo productInfo = new CommonProductInfo();
        productInfo.setProductCode("ed-m-0001");
        return productInfo;
    }

    private CommonCargoInfo buildCargoInfo() {
        CommonCargoInfo commonCargoInfo = new CommonCargoInfo();
        commonCargoInfo.setName("手机");
        commonCargoInfo.setQuantity(1);
        commonCargoInfo.setWeight(new BigDecimal(1));
        commonCargoInfo.setVolume(new BigDecimal(1));
        return commonCargoInfo;
    }
}