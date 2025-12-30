package com.anyi.common.mobileStat.mapper;


import com.anyi.common.mobileStat.domain.RecycleDataDailyBase;
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.anyi.common.mobileStat.response.RecycleStatVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 回收商统计日看板表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
public interface RecycleDataDailyBaseMapper extends BaseMapper<RecycleDataDailyBase> {

    RecycleStatVO recycleStat(CompanyStatDTO req);

}
