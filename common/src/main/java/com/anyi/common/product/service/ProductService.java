package com.anyi.common.product.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.product.domain.Product;
import com.anyi.common.product.domain.dto.ProductDTO;
import com.anyi.common.product.mapper.ProductMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/30
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    /**
     * 获取产品信息映射表
     *
     * @param ids 产品ID列表
     * @return 产品信息映射表
     */
    public Map<Long, ProductDTO> getProductInfoMap(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<ProductDTO> products = baseMapper.listProduct(ids);
        if (CollUtil.isEmpty(products)) {
            return Collections.emptyMap();
        }
        return products.stream()
                .filter(Objects::nonNull)
                .filter(o -> StrUtil.isNotBlank(o.getBrandLogo()))
                .collect(Collectors.toMap(ProductDTO::getId, Function.identity()));
    }
}