package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.sparrow.organize.employee.domain.EmpCfg;
import com.anyi.sparrow.organize.employee.domain.EmpCfgExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface EmpCfgMapper {
    long countByExample(EmpCfgExample example);

    int deleteByExample(EmpCfgExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EmpCfg record);

    int insertSelective(EmpCfg record);

    List<EmpCfg> selectByExampleWithRowbounds(EmpCfgExample example, RowBounds rowBounds);

    List<EmpCfg> selectByExample(EmpCfgExample example);

    EmpCfg selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") EmpCfg record, @Param("example") EmpCfgExample example);

    int updateByExample(@Param("record") EmpCfg record, @Param("example") EmpCfgExample example);

    int updateByPrimaryKeySelective(EmpCfg record);

    int updateByPrimaryKey(EmpCfg record);
}