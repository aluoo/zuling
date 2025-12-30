package com.anyi.common.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.common.product.domain.OrderLog;
import com.anyi.common.product.domain.dto.OrderLogDTO;
import com.anyi.common.product.mapper.OrderLogMapper;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/26
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderLogService extends ServiceImpl<OrderLogMapper, OrderLog> {
    public static final Long UNKNOWN_OPERATOR = -1L;
    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Transactional(rollbackFor = Exception.class)
    public void addLog(Long operator, Long orderId, int status, int operationStatus, String operation, String remark) {
        if (orderId == null) {
            return;
        }
        if (operator == null) {
            operator = UNKNOWN_OPERATOR;
        }
        OrderLog log = OrderLog.builder()
                .id(snowflakeIdService.nextId())
                .createBy(operator)
                .createTime(new Date())
                .deleted(false)
                .orderId(orderId)
                .status(status)
                .operationStatus(operationStatus)
                .operation(operation)
                .remark(remark)
                .build();
        this.save(log);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addLogBatch(Long operator, List<Long> orderIds, int status, int operationStatus, String operation, String remark) {
        if (CollUtil.isEmpty(orderIds)) {
            return;
        }
        if (operator == null) {
            operator = UNKNOWN_OPERATOR;
        }
        List<OrderLog> list = new ArrayList<>();
        for (Long orderId : orderIds) {
            list.add(OrderLog.builder()
                    .id(snowflakeIdService.nextId())
                    .createBy(operator)
                    .createTime(new Date())
                    .deleted(false)
                    .orderId(orderId)
                    .status(status)
                    .operationStatus(operationStatus)
                    .operation(operation)
                    .remark(remark)
                    .build());
        }
        if (CollUtil.isNotEmpty(list)) {
            this.saveBatch(list);
        }
    }

    public List<OrderLogDTO> listLog(Long orderId, Collection<Integer> status) {
        if (orderId == null) {
            return new ArrayList<>();
        }
        List<OrderLog> list = this.lambdaQuery()
                .in(CollUtil.isNotEmpty(status), OrderLog::getOperationStatus, status)
                .eq(OrderLog::getOrderId, orderId)
                .eq(OrderLog::getDeleted, false)
                .orderByDesc(OrderLog::getCreateTime)
                .list();
        return BeanUtil.copyToList(list, OrderLogDTO.class);
    }


    public OrderLog getLatestLogByOrderId(long orderId) {
        return lambdaQuery()
                .eq(OrderLog::getDeleted, false)
                .eq(OrderLog::getOrderId, orderId)
                .orderByDesc(OrderLog::getCreateTime)
                .orderByDesc(OrderLog::getId)
                .last("limit 1")
                .one();
    }

}