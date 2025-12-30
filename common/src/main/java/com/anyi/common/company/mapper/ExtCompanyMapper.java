package com.anyi.common.company.mapper;

import com.anyi.common.company.domain.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ExtCompanyMapper {

    Integer selectMaxCode(Long pId);

    void deleteChannel(@Param("code") String code, @Param("company") Company company);

    void updatePdept(@Param("pDeptId")Long pDeptId, @Param("cmpIds")List<Long> cmpIds);
}