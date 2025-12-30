package com.anyi.common.exchange.mapper;


import com.anyi.common.exchange.domain.MbExchangeOrder;
import com.anyi.common.mobileStat.domain.CompanyDataDailyBase;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 换机晒单表 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
public interface MbExchangeOrderMapper extends BaseMapper<MbExchangeOrder> {
    CompanyStatVO statPassGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("employeeIds") List<Long> employeeIds);

}
