package com.anyi.common.company.mapper;

import com.anyi.common.company.domain.Company;
import com.anyi.common.company.domain.CompanyExample;
import com.anyi.common.company.domain.RecycleRechargeLog;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.company.vo.RecycleCompanyVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface RecycleRechargeLogMapper extends BaseMapper<RecycleRechargeLog> {
}