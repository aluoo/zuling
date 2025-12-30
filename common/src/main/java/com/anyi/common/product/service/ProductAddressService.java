package com.anyi.common.product.service;

import com.anyi.common.product.domain.ProductAddress;
import com.anyi.common.product.mapper.ProductAddressMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/8
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductAddressService extends ServiceImpl<ProductAddressMapper, ProductAddress> {
}