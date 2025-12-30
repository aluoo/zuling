package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.common.dept.domain.Dept;
import com.anyi.sparrow.organize.employee.vo.DeptListRs;
import com.anyi.sparrow.organize.employee.vo.DeptListTotalRs;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ExtDeptMapper {

    List<DeptListRs> getAllChildDepts(@Param("companyId") Long companyId, @Param("deptCode") String deptCode);

    List<DeptListRs> getAllStatusChildDepts(@Param("companyId") Long companyId, @Param("deptCode") String deptCode);

    List<DeptListRs> getChildDepts(@Param("companyId") Long companyId, @Param("deptId") Long deptId);

    List<DeptListTotalRs> getChildDeptsAndTotal(@Param("companyId") Long companyId, @Param("deptId") Long deptId);

    Integer selectMaxCode(@Param("pdeptId") Long pdeptId);

    int queryTotalEm(@Param(value="deptCode")String deptCode, @Param(value="childDeptCode")String childDeptCode);

    void deleteChannel(@Param("code") String code, @Param("dept") Dept dept);

    void updateChildsCode(@Param("newPcode")String newPcode, @Param("oldPcode")String oldPcode, @Param("cmpIds")List cmpIds, @Param("updateTime") Date updateTime, @Param("updator") String updator);

    List<Long> queryCmpIdByCode(@Param("code") String code);

    int queryTotalEmAll(@Param("deptId") Long deptId, @Param("code") String code, @Param("companyType") Integer companyType);
}