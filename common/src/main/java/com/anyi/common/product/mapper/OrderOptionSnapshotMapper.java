package com.anyi.common.product.mapper;

import com.anyi.common.product.domain.OrderOptionSnapshot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/2
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface OrderOptionSnapshotMapper extends BaseMapper<OrderOptionSnapshot> {
}