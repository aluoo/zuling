package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.ShippingOrderAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/7
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface ShippingOrderAddressMapper extends BaseMapper<ShippingOrderAddress> {
}