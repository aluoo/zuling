package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.product.domain.ShippingOrderImage;
import com.anyi.common.product.mapper.ShippingOrderImageMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
public class ShippingOrderImageService extends ServiceImpl<ShippingOrderImageMapper, ShippingOrderImage> {

    @Transactional(rollbackFor = Exception.class)
    public void removeExistByShippingOrderId(Long shippingOrderId) {
        if (shippingOrderId == null) {
            return;
        }
        this.lambdaUpdate()
                .set(AbstractBaseEntity::getDeleted, true)
                .eq(ShippingOrderImage::getShippingOrderId, shippingOrderId)
                .eq(AbstractBaseEntity::getDeleted, false)
                .update(new ShippingOrderImage());
    }

    public List<String> listImagesUrlByShippingOrderId(Long shippingOrderId) {
        List<ShippingOrderImage> images = this.listImagesByShippingOrderId(shippingOrderId);
        return CollUtil.isEmpty(images)
                ? null
                : images.stream().map(ShippingOrderImage::getUrl).collect(Collectors.toList());
    }

    public List<ShippingOrderImage> listImagesByShippingOrderId(Long shippingOrderId) {
        if (shippingOrderId == null) {
            return null;
        }
        return this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(ShippingOrderImage::getShippingOrderId, shippingOrderId)
                .orderByAsc(ShippingOrderImage::getSort)
                .list();
    }

    public Map<Long, List<String>> buildImageUrlMapGroupByShippingOrderIds(List<Long> shippingOrderIds) {
        List<ShippingOrderImage> list = this.listByShippingOrderIds(shippingOrderIds);
        // 将list以shippingOrderId为key，转换为Map<Long, List<String>形式，Value为图片地址url
        return list.stream().collect(Collectors.groupingBy(ShippingOrderImage::getShippingOrderId, Collectors.mapping(ShippingOrderImage::getUrl, Collectors.toList())));
    }

    public Map<Long, List<ShippingOrderImage>> buildImageMapGroupByShippingOrderIds(List<Long> shippingOrderIds) {
        List<ShippingOrderImage> list = this.listByShippingOrderIds(shippingOrderIds);
        return this.buildImageMapGroupByShippingOrder(list);
    }

    public Map<Long, List<ShippingOrderImage>> buildImageMapGroupByShippingOrder(List<ShippingOrderImage> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(ShippingOrderImage::getShippingOrderId));
    }

    public List<ShippingOrderImage> listByShippingOrderIds(List<Long> shippingOrderIds) {
        if (CollUtil.isEmpty(shippingOrderIds)) {
            return null;
        }
        return this.lambdaQuery()
                .eq(AbstractBaseEntity::getDeleted, false)
                .in(ShippingOrderImage::getShippingOrderId, shippingOrderIds)
                .orderByAsc(ShippingOrderImage::getSort)
                .list();
    }
}