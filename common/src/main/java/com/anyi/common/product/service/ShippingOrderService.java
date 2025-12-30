package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.product.domain.ShippingOrder;
import com.anyi.common.product.domain.dto.ShippingOrderDTO;
import com.anyi.common.product.mapper.ShippingOrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/7
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ShippingOrderService extends ServiceImpl<ShippingOrderMapper, ShippingOrder> {

    public List<ShippingOrderDTO> listByOrderId(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return null;
        }
        return this.baseMapper.listByOrderId(orderIds);
    }

    public Map<Long, ShippingOrderDTO> buildShippingOrderMapByOrderIds(List<Long> orderIds) {
        List<ShippingOrderDTO> list = this.listByOrderId(orderIds);
        return this.buildShippingOrderMapByOrder(list);
    }

    public Map<Long, ShippingOrderDTO> buildShippingOrderMapByOrder(List<ShippingOrderDTO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(ShippingOrderDTO::getOrderId, Function.identity()));
    }
}