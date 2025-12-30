package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface BrandMapper extends BaseMapper<Brand> {
}