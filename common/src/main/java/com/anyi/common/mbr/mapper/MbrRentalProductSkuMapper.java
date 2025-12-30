package com.anyi.common.mbr.mapper;

import com.anyi.common.mbr.domain.MbrRentalProductSku;
import com.anyi.common.mbr.response.MbrProductDTO;
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
public interface MbrRentalProductSkuMapper extends BaseMapper<MbrRentalProductSku> {

    /**
     * 根据SKUID列表查询产品信息
     *
     * @param skuIds 产品ID列表
     * @return 产品信息列表
     */
    List<MbrProductDTO> listProduct(@Param("skuIds") List<Long> skuIds);

}
