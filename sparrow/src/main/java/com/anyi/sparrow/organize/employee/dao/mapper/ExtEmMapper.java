package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.employee.domain.Employee;
import com.anyi.sparrow.organize.employee.vo.EmInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExtEmMapper {

    Long verifyManager(@Param("user") LoginUser user, @Param("childId") Long childId);

    List<EmInfo> queryByNameOrMobile(@Param("express") String express, @Param("user") LoginUser user, @Param("parent") Dept parent, @Param("hadManger") boolean hadManger);

    void deleteChannel(@Param("code") String code, @Param("employee") Employee employee);

    void updateDeptCode(@Param("cmpIds")List cmpIds, @Param("deptCode") String deptCode, @Param("updateTime") Date updateTime, @Param("updator") String updator);

    List<Map<String, String>> queryTokenByDeptCode(@Param("cmpIds")List cmpIds, @Param("deptCode") String deptCode, @Param("currDate") Date currDate);
}