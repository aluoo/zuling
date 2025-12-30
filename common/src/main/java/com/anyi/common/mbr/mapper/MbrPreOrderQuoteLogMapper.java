package com.anyi.common.mbr.mapper;

import com.anyi.common.mbr.domain.MbrPreOrderQuoteLog;
import com.anyi.common.mbr.response.PreOrderQuoteLogCountDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数保产品选项表 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
public interface MbrPreOrderQuoteLogMapper extends BaseMapper<MbrPreOrderQuoteLog> {

    /**
     * 根据订单ID分组统计订单报价日志数量
     *
     * @param orderIds 订单ID列表
     * @return 订单ID和订单报价日志数量的映射列表
     */
    List<PreOrderQuoteLogCountDTO> countGroupByOrderIds(@Param("orderIds") List<Long> orderIds);

}
