package com.anyi.common.mobileStat.mapper;


import com.anyi.common.mobileStat.domain.CompanyDataDailyBase;
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 门店统计日看板表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-08
 */
public interface CompanyDataDailyBaseMapper extends BaseMapper<CompanyDataDailyBase> {

    CompanyStatVO companyStat(CompanyStatDTO req);

}
