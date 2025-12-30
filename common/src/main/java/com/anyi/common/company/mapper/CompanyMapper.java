package com.anyi.common.company.mapper;

import com.anyi.common.company.domain.Company;

import com.anyi.common.company.domain.CompanyExample;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.company.vo.RecycleCompanyVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CompanyMapper extends BaseMapper<Company> {
    long countByExample(CompanyExample example);

    List<Company> selectByExampleWithRowbounds(CompanyExample example, RowBounds rowBounds);

    List<Company> selectByExample(CompanyExample example);

    List<Long> queryAutoSettleCompanyIds();

    List<AgencyCompanyVO> getByEmployee(@Param("req") CompanyReq req);

    List<RecycleCompanyVO> recycleList(@Param("req") CompanyReq req);
}