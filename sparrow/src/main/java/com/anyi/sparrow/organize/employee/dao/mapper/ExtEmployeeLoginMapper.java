package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.sparrow.organize.employee.domain.EmployeeLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtEmployeeLoginMapper {
//    List<EmployeeLogin> selectByCmpId(@Param("cmpId") Long cmpId);
//
//    int deleteByCmpId(@Param("cmpId") Long cmpId);

    List<EmployeeLogin> selectByDeptCode(@Param("deptCode") String deptCode);

    int deleteByDeptCode(@Param("deptCode") String deptCode);
}