package com.anyi.common.product.service;

import com.anyi.common.product.domain.Brand;
import com.anyi.common.product.mapper.BrandMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class BrandService extends ServiceImpl<BrandMapper, Brand> {
}