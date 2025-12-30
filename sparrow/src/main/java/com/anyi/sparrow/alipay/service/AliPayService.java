package com.anyi.sparrow.alipay.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.constant.RedisLockKeyConstants;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.OrderCustomerReceivePayment;
import com.anyi.common.product.domain.enums.OrderOperationEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.enums.ReceivePaymentStatusEnum;
import com.anyi.common.product.domain.request.AlipayReceivePaymentTransferReq;
import com.anyi.common.product.service.OrderCustomerReceivePaymentService;
import com.anyi.common.product.service.OrderLogService;
import com.anyi.common.product.service.OrderService;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.DistributionLockUtil;
import com.anyi.common.util.MoneyUtil;
import com.anyi.sparrow.alipay.config.AlipayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class AliPayService {
    @Resource
    private AlipayProperties alipayProperties;
    @Autowired
    SnowflakeIdService snowflakeIdService;
    @Autowired
    private OrderCustomerReceivePaymentService orderCustomerReceivePaymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private CommonSysDictService dictService;
    @Autowired
    private DistributionLockUtil distributionLockUtil;

    private static AlipayClient alipayClient = null;

    @PostConstruct
    public void init() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(alipayProperties.getServerUrl());
        alipayConfig.setAppId(alipayProperties.getAppId());
        alipayConfig.setFormat("json");
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        //设置应用私钥
        alipayConfig.setPrivateKey(alipayProperties.getPrivateKey());
        //设置应用公钥证书路径
        alipayConfig.setAppCertPath(alipayProperties.getAppCertPath());
        //设置支付宝公钥证书路径
        alipayConfig.setAlipayPublicCertPath(alipayProperties.getAlipayCertPath());
        //设置支付宝根证书路径
        alipayConfig.setRootCertPath(alipayProperties.getAlipayRootCertPath());
        //设置连接池中的最大可缓存的空闲连接数
        alipayConfig.setMaxIdleConnections(5);
        //连接超时，单位：毫秒，默认3000
        alipayConfig.setConnectTimeout(3000);
        //读取超时，单位：毫秒，默认15000
        alipayConfig.setReadTimeout(15000);
        //空闲连接存活时间，单位：毫秒，默认10000L
        alipayConfig.setKeepAliveDuration(10000L);
        alipayClient = new DefaultAlipayClient(alipayConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    public void receivePaymentTransfer(AlipayReceivePaymentTransferReq req) {
        String lockKey = StrUtil.format(RedisLockKeyConstants.ALIPAY_RECEIVE_PAYMENT_TRANSFER_LOCK_KEY, req.getOrderId());
        distributionLockUtil.lock(
                () -> receivePaymentTransferBiz(req),
                0,
                () -> lockKey,
                "正在收款中，请稍等",
                null);
    }

    private void receivePaymentTransferBiz(AlipayReceivePaymentTransferReq req) {
        Long orderId = req.getOrderId();
        Long paymentId = req.getPaymentId();
        String authorizationCode = req.getAuthorizationCode();

        Order order = getOrder(orderId);

        OrderCustomerReceivePayment payment = getPayment(paymentId, orderId);
        // get openId
        String openId = getAlipayOpenId(authorizationCode);

        // update payment set openId
        orderCustomerReceivePaymentService.lambdaUpdate()
                .set(OrderCustomerReceivePayment::getOpenId, openId)
                .eq(OrderCustomerReceivePayment::getId, paymentId)
                .eq(OrderCustomerReceivePayment::getOrderId, orderId)
                .update(new OrderCustomerReceivePayment());
        // transfer
        AlipayFundTransUniTransferResponse transferRes = transfer(payment.getOutBizNo(), openId, MoneyUtil.fenToYuan(payment.getAmount()));
        // todo query
        // update payment
        orderCustomerReceivePaymentService.lambdaUpdate()
                .set(OrderCustomerReceivePayment::getOpenId, openId)
                .set(OrderCustomerReceivePayment::getStatus, ReceivePaymentStatusEnum.PAYED.getCode())
                .set(OrderCustomerReceivePayment::getRemoteResp, JSONUtil.toJsonStr(transferRes))
                .set(OrderCustomerReceivePayment::getAlipayOrderId, transferRes.getOrderId())
                .set(OrderCustomerReceivePayment::getPayFundOrderId, transferRes.getPayFundOrderId())
                .set(StrUtil.isNotBlank(transferRes.getTransDate()), OrderCustomerReceivePayment::getReceiveTime, DateUtil.parse(transferRes.getTransDate()))
                .eq(OrderCustomerReceivePayment::getId, paymentId)
                .eq(OrderCustomerReceivePayment::getOrderId, orderId)
                .update(new OrderCustomerReceivePayment());
        // update order
        Integer finalPrice = order.getFinalPrice();
        // 根据收款时间开始计算，单笔交易金额大于等于200元，截止至当天发货；否则截止至本周末发货
        DateTime transferDate = DateUtil.parse(transferRes.getTransDate());
        Date shippingOverdueTime = DateUtil.endOfDay(transferDate).toJdkDate();
        if (finalPrice < dictService.getShippingOverdueTimeThresholdPrice()) {
            shippingOverdueTime = DateUtil.endOfWeek(transferDate).toJdkDate();
        }

        boolean success = orderService.lambdaUpdate()
                .set(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .set(Order::getShippingOverdueTime, shippingOverdueTime)
                .eq(Order::getId, orderId)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_PAYMENT.getCode())
                .update(new Order());
        if (success) {
            orderLogService.addLog(
                    -1L,
                    orderId,
                    OrderStatusEnum.PENDING_SHIPMENT.getCode(),
                    OrderOperationEnum.PAY.getCode(),
                    OrderOperationEnum.PAY.getDesc(),
                    OrderOperationEnum.PAY.getRemark());
        }
    }

    private Order getOrder(Long orderId) {
        Order order = orderService.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getId, orderId)
                .one();
        if (order == null) {
            log.info("getOrder.error: order not found - {}", orderId);
            throw new BusinessException(BizError.MB_ORDER_NOT_EXISTS);
        }
        if (order.getStatus() < OrderStatusEnum.PENDING_PAYMENT.getCode()) {
            log.info("getOrder.error: order status not correct - {}-{}", orderId, EnumUtil.getBy(OrderStatusEnum::getCode, order.getStatus()).getDesc());
            throw new BusinessException(BizError.MB_ORDER_OPERATION_ERROR_STATUS);
        }
        if (order.getStatus() > OrderStatusEnum.PENDING_PAYMENT.getCode()) {
            log.info("getOrder.error: order already payed - {}", orderId);
            throw new BusinessException(99999, "已成功收款");
        }
        return order;
    }

    private OrderCustomerReceivePayment getPayment(Long paymentId, Long orderId) {
        OrderCustomerReceivePayment payment = orderCustomerReceivePaymentService.lambdaQuery()
                .eq(OrderCustomerReceivePayment::getId, paymentId)
                .eq(OrderCustomerReceivePayment::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .one();
        if (payment == null) {
            log.info("getPayment.error: payment not found - {}", paymentId);
            throw new BusinessException(-1, "收款码已失效");
        }

        int alipayReceivePaymentExpiredMinutes = dictService.getAlipayReceivePaymentExpiredMinutes();
        if (alipayReceivePaymentExpiredMinutes > 0) {
            // 校验超时失效
            DateTime expiredTime = DateUtil.offset(DateUtil.date(payment.getCreateTime()), DateField.MINUTE, alipayReceivePaymentExpiredMinutes);
            if (DateUtil.compare(DateUtil.date(), expiredTime) > 0) {
                throw new BusinessException(-1, "收款码已失效");
            }
        }

        if (payment.getStatus().equals(ReceivePaymentStatusEnum.PAYED.getCode())) {
            log.info("getPayment.error: order already payed order-{}, payment-{}", orderId, paymentId);
            throw new BusinessException(99999, "已成功收款");
        }
        return payment;
    }

    private String getAlipayOpenId(String authorizationCode) {
        String openId;
        try {
            AlipaySystemOauthTokenResponse tokenResponse = getOpenId(authorizationCode);
            log.info("getAlipayOpenId.tokenResp-{}", JSONUtil.toJsonStr(tokenResponse));
            openId = tokenResponse.getOpenId();
        } catch (Exception e) {
            log.info("getAlipayOpenId.error: {}", e.getMessage());
            throw new BusinessException(99999, "支付宝授权信息获取失败");
        }
        if (StrUtil.isBlank(openId)) {
            log.info("getAlipayOpenId.error: openId is null");
            throw new BusinessException(99999, "支付宝授权信息获取失败");
        }
        return openId;
    }

    /**
     * 获取openId
     *
     * @param authorizationCode
     * @return
     */
    public AlipaySystemOauthTokenResponse getOpenId(String authorizationCode) {
        // 构造请求参数以调用接口
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        // 设置授权方式
        request.setGrantType("authorization_code");
        // 设置授权码
        request.setCode(authorizationCode);
        AlipaySystemOauthTokenResponse response = null;
        log.info("支付宝获取OPENID请求参数:{}:{}", authorizationCode, JSON.toJSONString(request));
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.info("支付宝换取授权访问令牌请求错误:{}", e.getMessage());
            throw new BusinessException(99999, "支付宝换取授权访问令牌网络错误");
        }
        log.info("支付宝获取OPENID返回参数{}", JSON.toJSONString(response));
        if (!response.isSuccess()) {
            log.info("支付宝换取授权访问令牌业务错误:{}", response.getSubMsg());
            throw new BusinessException(99999, "支付宝换取授权访问令牌业务错误");
        }
        if (StrUtil.isBlank(response.getOpenId())) {
            log.info("支付宝换取授权访问令牌业务错误: openId is null");
            throw new BusinessException(99999, "支付宝换取授权访问令牌业务错误");
        }
        return response;
    }

    /**
     * 转账
     * 支付宝转账，同一个商户单号调接口不会报错，会返回成功，但是转账成功时间是第一次成功时间，只会打一次钱。
     * @param openId
     * @param amount
     * @return
     */
    public AlipayFundTransUniTransferResponse transfer(String outBizNo, String openId, String amount) {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        //商户订单号
        model.setOutBizNo(outBizNo);
        // 设置订单总金额
        BigDecimal bd = new BigDecimal(amount);
        //调用函数 参数2表示保留两位小数，ROUND_HALF_UP表示四舍五入
        BigDecimal bd1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        model.setTransAmount(bd1.toString());
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOrderTitle("手机转账款");
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(openId);
        payeeInfo.setIdentityType("ALIPAY_OPEN_ID");
        //payeeInfo.setName("陈健");
        model.setPayeeInfo(payeeInfo);
        request.setBizModel(model);
        AlipayFundTransUniTransferResponse response = null;
        log.info("支付宝转账请求参数:{}:{}", outBizNo, JSON.toJSONString(request));
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.info("支付宝转账请求错误:{}", e.getMessage());
            throw new BusinessException(99999, "支付宝转账网络错误");
        }
        log.info("支付宝转账返回参数{}", JSON.toJSONString(response));
        //当subCode为SYSTEM_ERROR 不确定转账成功还是失败需特殊处理，在发起一次查询
        if (!response.isSuccess() && !response.getSubCode().equals("SYSTEM_ERROR")) {
            log.info("支付宝转账业务错误:{}", response.getSubMsg());
            throw new BusinessException(99999, "支付宝转账业务错误");
        }
        return response;
    }


    /**
     * 查询转账状态
     *
     * @param outBizNo
     * @return
     */
    public AlipayFundTransCommonQueryResponse query(String outBizNo) {
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");
        model.setOutBizNo(outBizNo);
        request.setBizModel(model);
        AlipayFundTransCommonQueryResponse response = null;
        log.info("支付宝查询请求参数:{}:{}", outBizNo, JSON.toJSONString(request));
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.info("支付宝查询请求错误:{}", e.getMessage());
            throw new BusinessException(99999, "支付宝查询网络错误");
        }
        log.info("支付宝查询返回参数{}", JSON.toJSONString(response));
        if (!response.isSuccess()) {
            log.info("支付宝查询业务错误:{}", response.getSubMsg());
            throw new BusinessException(99999, "支付宝查询业务错误");
        }
        return response;
    }
}