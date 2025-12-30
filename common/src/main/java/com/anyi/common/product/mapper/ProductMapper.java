package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.Product;
import com.anyi.common.product.domain.dto.ProductDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/23
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据产品ID列表查询产品信息
     *
     * @param productIds 产品ID列表
     * @return 产品信息列表
     */
    List<ProductDTO> listProduct(@Param("productIds") List<Long> productIds);
}