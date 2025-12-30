package com.anyi.common.product.service;

import com.anyi.common.product.domain.ProductOption;
import com.anyi.common.product.mapper.ProductOptionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/24
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductOptionService extends ServiceImpl<ProductOptionMapper, ProductOption> {

    public List<Long> getOptionIdsByProductId(Long productId) {
        if (productId == null) {
            return new ArrayList<>();
        }
        return this.lambdaQuery()
                .eq(ProductOption::getProductId, productId)
                .list()
                .stream().map(ProductOption::getOptionId).collect(Collectors.toList());
    }

}