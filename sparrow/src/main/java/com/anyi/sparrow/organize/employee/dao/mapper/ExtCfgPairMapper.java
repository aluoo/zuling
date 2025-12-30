package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.sparrow.organize.employee.vo.EmpCfgVo;
import org.apache.ibatis.annotations.Param;

public interface ExtCfgPairMapper {
    String getMax(Integer biz);

    EmpCfgVo getByIdAndBiz(@Param("empId") Long empId, @Param("biz") Integer biz);
}