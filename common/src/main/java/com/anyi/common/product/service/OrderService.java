package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.dto.OrderShippingCountDTO;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    public List<OrderShippingCountDTO> conditionCountOrderGroupByRecycler(OrderShippingCountDTO req) {
        List<OrderShippingCountDTO> list = this.getBaseMapper().conditionCountOrderGroupByRecycler(req);
        if (CollUtil.isEmpty(list)) {
            list = new ArrayList<>();
        }
        return list;
    }

    public Map<Long, Integer> getOverdueCountMap(OrderShippingCountDTO req) {
        req.setStatus(OrderStatusEnum.PENDING_SHIPMENT.getCode());
        req.setCountType(OrderShippingCountDTO.CountType.OVERDUE.getValue());
        List<OrderShippingCountDTO> list = this.conditionCountOrderGroupByRecycler(req);
        return list.stream()
                .filter(o -> o.getRecyclerCompanyId() != null && o.getCount() != null)
                .collect(Collectors.toMap(OrderShippingCountDTO::getRecyclerCompanyId, OrderShippingCountDTO::getCount));
    }

    public Map<Long, Integer> getPendingShipmentCountMap(OrderShippingCountDTO req) {
        req.setStatus(OrderStatusEnum.PENDING_SHIPMENT.getCode());
        req.setCountType(OrderShippingCountDTO.CountType.PENDING_SHIPMENT.getValue());
        List<OrderShippingCountDTO> list = this.conditionCountOrderGroupByRecycler(req);
        return list.stream()
                .filter(o -> o.getRecyclerCompanyId() != null && o.getCount() != null)
                .collect(Collectors.toMap(OrderShippingCountDTO::getRecyclerCompanyId, OrderShippingCountDTO::getCount));
    }

    public Map<Long, Integer> getPendingReceiptCountMap(OrderShippingCountDTO req) {
        req.setStatus(OrderStatusEnum.PENDING_RECEIPT.getCode());
        req.setCountType(OrderShippingCountDTO.CountType.PENDING_RECEIPT.getValue());
        List<OrderShippingCountDTO> list = this.conditionCountOrderGroupByRecycler(req);
        return list.stream()
                .filter(o -> o.getRecyclerCompanyId() != null && o.getCount() != null)
                .collect(Collectors.toMap(OrderShippingCountDTO::getRecyclerCompanyId, OrderShippingCountDTO::getCount));
    }
}