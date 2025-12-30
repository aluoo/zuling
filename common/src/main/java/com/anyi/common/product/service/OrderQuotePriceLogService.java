package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.OrderQuotePriceLog;
import com.anyi.common.product.domain.dto.ConfirmQuoteInfoDTO;
import com.anyi.common.product.domain.dto.InitOrderQuotePriceLogDTO;
import com.anyi.common.product.domain.dto.OrderQuoteInfoDTO;
import com.anyi.common.product.domain.dto.OrderQuotePriceLogCountDTO;
import com.anyi.common.product.domain.enums.OrderQuoteLogStatusEnum;
import com.anyi.common.product.domain.enums.OrderQuoteLogSubStatusEnum;
import com.anyi.common.product.domain.request.OrderQuoteQueryReq;
import com.anyi.common.product.domain.response.RecyclerQuoteCountInfoVO;
import com.anyi.common.product.mapper.OrderQuotePriceLogMapper;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/1
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderQuotePriceLogService extends ServiceImpl<OrderQuotePriceLogMapper, OrderQuotePriceLog> {
    @Autowired
    private SnowflakeIdService snowflakeIdService;

    public RecyclerQuoteCountInfoVO countQuoteLog(Long recyclerCompanyId, Long recyclerEmployeeId) {
        RecyclerQuoteCountInfoVO vo = RecyclerQuoteCountInfoVO.builder().build();
        if (recyclerCompanyId == null) {
            return vo;
        }
        return this.baseMapper.countQuoteLog(recyclerCompanyId, recyclerEmployeeId);
    }

    @Transactional(rollbackFor = Exception.class)
    public InitOrderQuotePriceLogDTO initQuoteLog(Long orderId, List<Long> recyclerIds) {
        if (orderId == null || CollUtil.isEmpty(recyclerIds)) {
            return InitOrderQuotePriceLogDTO.builder().initCount(0).build();
        }
        List<OrderQuotePriceLog> list = new ArrayList<>();
        recyclerIds.forEach(cid -> {
            OrderQuotePriceLog o = OrderQuotePriceLog.builder()
                    .id(snowflakeIdService.nextId())
                    .orderId(orderId)
                    .companyId(cid)
                    .status(OrderQuoteLogStatusEnum.PENDING_QUOTE.getCode())
                    .subStatus(OrderQuoteLogSubStatusEnum.PENDING_QUOTE.getCode())
                    .build();
            list.add(o);
        });
        if (CollUtil.isNotEmpty(list)) {
            this.saveBatch(list);
        }
        List<Long> ids = list.stream().map(OrderQuotePriceLog::getId).collect(Collectors.toList());
        return InitOrderQuotePriceLogDTO.builder().orderQuotePriceLogIds(ids).initCount(list.size()).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ConfirmQuoteInfoDTO confirmQuote(Long orderId, Long confirmedQuoteId) {
        // 确认交易，将确认的报价记录子状态更新为已确认报价，其余更新为作废，未入选
        this.lambdaUpdate()
                .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.CONFIRMED.getCode())
                .eq(OrderQuotePriceLog::getId, confirmedQuoteId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new OrderQuotePriceLog());
        this.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.CANCELED.getCode())
                .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.NOT_CONFIRM.getCode())
                .ne(OrderQuotePriceLog::getId, confirmedQuoteId)
                .eq(OrderQuotePriceLog::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new OrderQuotePriceLog());
        List<Long> notConfirmedQuoteIds = this.lambdaQuery()
                .select(OrderQuotePriceLog::getId)
                .ne(OrderQuotePriceLog::getId, confirmedQuoteId)
                .eq(OrderQuotePriceLog::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list().stream().map(OrderQuotePriceLog::getId).collect(Collectors.toList());
        return ConfirmQuoteInfoDTO.builder().confirmedQuoteId(confirmedQuoteId).notConfirmedQuoteIds(notConfirmedQuoteIds).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Long> cancelTrade(Long orderId) {
        // 取消交易，将关联的报价记录状态更新为已作废，交易取消
        List<Long> ids = this.lambdaQuery()
                .select(OrderQuotePriceLog::getId)
                .eq(OrderQuotePriceLog::getOrderId, orderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                // 主动取消，拒绝报价状态保留不变
                .ne(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.REJECT_QUOTE.getCode())
                .list()
                .stream()
                .map(OrderQuotePriceLog::getId)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ids)) {
            this.lambdaUpdate()
                    .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.CANCELED.getCode())
                    .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.CANCEL_TRADE.getCode())
                    .eq(OrderQuotePriceLog::getOrderId, orderId)
                    .eq(AbstractBaseEntity::getDeleted, false)
                    .in(OrderQuotePriceLog::getId, ids)
                    .update(new OrderQuotePriceLog());
        }
        return ids;
    }

    @Transactional(rollbackFor = Exception.class)
    public void refundTrade(Long confirmedQuoteId) {
        // 交易退款，将确认交易的报价记录状态更新为已作废，交易退款
        this.lambdaUpdate()
                .set(OrderQuotePriceLog::getStatus, OrderQuoteLogStatusEnum.CANCELED.getCode())
                .set(OrderQuotePriceLog::getSubStatus, OrderQuoteLogSubStatusEnum.REFUND_TRADE.getCode())
                .eq(OrderQuotePriceLog::getId, confirmedQuoteId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new OrderQuotePriceLog());
    }

    public Integer countByOrderId(Long orderId) {
        if (orderId == null) {
            return 0;
        }
        Map<Long, Integer> collect = countGroupByOrderIds(Collections.singletonList(orderId));
        return Optional.ofNullable(collect.get(orderId)).orElse(0);
    }

    public Map<Long, Integer> countGroupByOrderIds(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return Collections.emptyMap();
        }
        List<OrderQuotePriceLogCountDTO> list = this.baseMapper.countGroupByOrderIds(orderIds);
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(
                Collectors.toMap(OrderQuotePriceLogCountDTO::getOrderId, OrderQuotePriceLogCountDTO::getNum)
        );
    }

    public List<OrderQuoteInfoDTO> listQuoteInfoByOrderId(OrderQuoteQueryReq req) {
        return this.baseMapper.listQuoteInfoByOrderId(req);
    }

    public OrderQuoteInfoDTO getQuoteInfo(OrderQuoteQueryReq req) {
        List<OrderQuoteInfoDTO> list = this.baseMapper.listQuoteInfoByOrderId(req);
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }
}