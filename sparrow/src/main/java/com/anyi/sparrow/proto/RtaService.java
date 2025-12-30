package com.anyi.sparrow.proto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.exchange.dto.RtaReq;
import com.anyi.common.util.SpringContextUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author chenjian
 * @Description
 * @Date 2024/7/29
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class RtaService {
    public static final String RTA_URL_PROD = "http://rta.totiot.com/srv/v1/rta/7";
    public static final String RTA_URL_TEST = "http://rta.totiot.com/srv/v1/rta/7";

    public Boolean postRta(RtaReq req) {
        // 创建请求
        String requestUrl = SpringContextUtil.isProduction() ? RTA_URL_PROD : RTA_URL_TEST;
        HttpRequest request = HttpRequest.post(requestUrl)
                .header("Content-Type", "application/x-protobuf"); // 设置Content-Type请求头

        ExchangeMessage.Request.Builder builder = ExchangeMessage.Request.newBuilder();
        builder.addAllTokens(Arrays.asList(req.getToken()));
        builder.setChannel(req.getChannel());

        ExchangeMessage.Device.Builder device = ExchangeMessage.Device.newBuilder();
        if(StrUtil.isNotBlank(req.getAid())){
            device.setAid(req.getAid());
        }
        if(StrUtil.isNotBlank(req.getOaid())){
            device.setOaid(req.getOaid());
        }
        if(StrUtil.isNotBlank(req.getImei())){
            device.setImei(req.getImei());
        }
        if(StrUtil.isNotBlank(req.getIp())){
            device.setIp(req.getIp());
        }
        if(StrUtil.isNotBlank(req.getModel())){
            device.setModel(req.getModel());
        }

        builder.setDevice(device);

        // 设置protobuf数据
        ExchangeMessage.Request messageRequest = builder.build();


        log.info("请求RTA参数:{}",messageRequest.toString());
        request.body(builder.build().toByteArray());
        try {
            // 发送请求
            HttpResponse response = request.execute();
            ExchangeMessage.Response messageResponse = ExchangeMessage.Response.parseFrom(response.bodyBytes());
            log.info("请求RTA响应结果:{}:{}:{}",messageResponse.getStatusCode(),messageResponse.getMsg(),messageResponse.getTokensCount());
            if(messageResponse.getStatusCode()!=0 || messageResponse.getTokensCount() == 0){
                throw new BusinessException(99999,"RTA校验不通过");
            }
        } catch (InvalidProtocolBufferException e) {
            throw new BusinessException(99999,"RTA请求异常");
        }
        return true;
    }


}