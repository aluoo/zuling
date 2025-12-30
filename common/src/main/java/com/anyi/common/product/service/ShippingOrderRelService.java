package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.product.domain.ShippingOrderRel;
import com.anyi.common.product.mapper.ShippingOrderRelMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class ShippingOrderRelService extends ServiceImpl<ShippingOrderRelMapper, ShippingOrderRel> {

    public List<ShippingOrderRel> listByShippingOrderId(Long shippingOrderId) {
        if (shippingOrderId == null) {
            return null;
        }
        return this.lambdaQuery().eq(ShippingOrderRel::getShippingOrderId, shippingOrderId).list();
    }

    public List<Long> listOrderIdsByShippingOrderId(Long shippingOrderId) {
        List<ShippingOrderRel> relations = this.listByShippingOrderId(shippingOrderId);
        return CollUtil.isNotEmpty(relations)
                ? relations.stream().map(ShippingOrderRel::getOrderId).filter(Objects::nonNull).collect(Collectors.toList())
                : null;
    }

    public List<Long> listOrderIdsByShippingOrderIds(List<Long> shippingOrderIds) {
        List<ShippingOrderRel> relations = this.lambdaQuery().in(ShippingOrderRel::getShippingOrderId, shippingOrderIds).list();
        return CollUtil.isNotEmpty(relations)
                ? relations.stream().map(ShippingOrderRel::getOrderId).filter(Objects::nonNull).collect(Collectors.toList())
                : null;
    }

    public Map<Long, Long> countByShippingOrderIds(List<Long> shippingOrderIds) {
        if (CollUtil.isEmpty(shippingOrderIds)) {
            return null;
        }
        List<ShippingOrderRel> list = this.lambdaQuery()
                .in(ShippingOrderRel::getShippingOrderId, shippingOrderIds)
                .list();
        // 统计每个shippingOrderId对应的数量，返回Map<Long, Integer>
        return list.stream().collect(Collectors.groupingBy(ShippingOrderRel::getShippingOrderId, Collectors.counting()));
    }
}