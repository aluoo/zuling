package com.anyi.common.insurance.mapper;

import com.anyi.common.insurance.domain.DiInsuranceOrder;
import com.anyi.common.mobileStat.domain.CompanyDataDailyBase;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
public interface DiInsuranceOrderMapper extends BaseMapper<DiInsuranceOrder> {

    CompanyStatVO statGroupByEmployee(@Param("beginTime") Date beginTime, @Param("endTime") Date endTim, @Param("employeeIds") List<Long> employeeIds);

}
