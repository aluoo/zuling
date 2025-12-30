package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.OrderQuotePriceLog;
import com.anyi.common.product.domain.dto.OrderQuoteInfoDTO;
import com.anyi.common.product.domain.dto.OrderQuotePriceLogCountDTO;
import com.anyi.common.product.domain.request.OrderQuoteQueryReq;
import com.anyi.common.product.domain.response.RecyclerQuoteCountInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/1
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface OrderQuotePriceLogMapper extends BaseMapper<OrderQuotePriceLog> {

    /**
     * 根据订单ID分组统计订单报价日志数量
     *
     * @param orderIds 订单ID列表
     * @return 订单ID和订单报价日志数量的映射列表
     */
    List<OrderQuotePriceLogCountDTO> countGroupByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 根据订单ID查询订单报价信息列表
     * @param req OrderQuoteQueryReq
     * @return 订单报价信息列表
     */
    List<OrderQuoteInfoDTO> listQuoteInfoByOrderId(@Param("req")OrderQuoteQueryReq req);

    /**
     * 统计每个状态下的报价日志数量
     * @param recyclerCompanyId 回收商公司ID
     * @param recyclerEmployeeId 回收商员工ID
     * @return RecyclerQuoteCountInfoVO
     */
    RecyclerQuoteCountInfoVO countQuoteLog(@Param("recyclerCompanyId")Long recyclerCompanyId, @Param("recyclerEmployeeId") Long recyclerEmployeeId);
}