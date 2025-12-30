package com.anyi.common.insurance.mapper;

import com.anyi.common.insurance.domain.DiProductInsurancePrice;
import com.anyi.common.insurance.domain.dto.DiProductInsurancePriceDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface DiProductInsurancePriceMapper extends BaseMapper<DiProductInsurancePrice> {

    DiProductInsurancePriceDTO getInfoByPriceInterval(@Param("req") DiProductInsurancePriceDTO req);
}