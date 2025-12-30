package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.ShippingOrder;
import com.anyi.common.product.domain.dto.ShippingOrderDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/7
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface ShippingOrderMapper extends BaseMapper<ShippingOrder> {
    @Select("select o.* from mb_shipping_order o inner join mb_shipping_order_rel r on o.id = r.shipping_order_id where r.order_id = #{orderId}")
    ShippingOrder getByOrderId(@Param("orderId") Long orderId);

    List<ShippingOrderDTO> listByOrderId(@Param("orderIds") List<Long> orderIds);
}