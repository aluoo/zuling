package com.anyi.common.service;

import com.anyi.common.domain.entity.PayApplyRecord;
import com.anyi.common.domain.mapper.PayApplyRecordMapper;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class PayApplyRecordService extends ServiceImpl<PayApplyRecordMapper, PayApplyRecord> {
    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Async
    public void addRecord(Long orderId, Long userId, String payEnter, String outTradeNo, String actualMchId, Integer bizType) {
        if (orderId == null) {
            return;
        }
        PayApplyRecord bean = PayApplyRecord.builder()
                .id(snowflakeIdService.nextId())
                .createTime(new Date())
                .userId(userId)
                .orderId(orderId)
                .payEnter(payEnter)
                .actualMchId(actualMchId)
                .outTradeNo(outTradeNo)
                .bizType(bizType)
                .build();
        this.save(bean);
    }
}