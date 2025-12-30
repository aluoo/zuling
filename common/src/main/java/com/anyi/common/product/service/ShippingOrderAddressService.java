package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.ShippingOrderAddress;
import com.anyi.common.product.mapper.ShippingOrderAddressMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ShippingOrderAddressService extends ServiceImpl<ShippingOrderAddressMapper, ShippingOrderAddress> {

    @Transactional(rollbackFor = Exception.class)
    public void removeExistByShippingOrderId(Long shippingOrderId) {
        if (shippingOrderId == null) {
            return;
        }
        this.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(ShippingOrderAddress::getShippingOrderId, shippingOrderId)
                .update(new ShippingOrderAddress());
    }

    public ShippingOrderAddress getAddressByShippingOrderAndType(Long shippingOrderId, Integer type) {
        if (shippingOrderId == null || type == null) {
            return null;
        }
        return this.lambdaQuery()
                .eq(ShippingOrderAddress::getType, type)
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(ShippingOrderAddress::getShippingOrderId, shippingOrderId)
                .one();
    }

    public Map<Long, List<ShippingOrderAddress>> buildAddressMapGroupByShippingOrderIds(List<Long> shippingOrderIds) {
        List<ShippingOrderAddress> list = this.listByShippingOrderIds(shippingOrderIds);
        return this.buildAddressMapGroupByShippingOrder(list);
    }

    public Map<Long, List<ShippingOrderAddress>> buildAddressMapGroupByShippingOrder(List<ShippingOrderAddress> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        // 将list以shippingOrderId为key，转换为Map<Long, List<ShippingOrderAddress>>
        return list.stream().collect(Collectors.groupingBy(ShippingOrderAddress::getShippingOrderId));
    }

    public Map<Integer, ShippingOrderAddress> buildAddressMapByType(List<ShippingOrderAddress> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(ShippingOrderAddress::getType, Function.identity()));
    }

    public List<ShippingOrderAddress> listByShippingOrderIds(List<Long> shippingOrderIds) {
        if (CollUtil.isEmpty(shippingOrderIds)) {
            return null;
        }
        return this.lambdaQuery()
                .in(ShippingOrderAddress::getShippingOrderId, shippingOrderIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .list();
    }
}