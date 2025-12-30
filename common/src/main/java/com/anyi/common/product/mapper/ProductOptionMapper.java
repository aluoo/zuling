package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.ProductOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/24
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface ProductOptionMapper extends BaseMapper<ProductOption> {
}