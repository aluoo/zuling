package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.sparrow.organize.employee.domain.CfgPair;
import com.anyi.sparrow.organize.employee.domain.CfgPairExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CfgPairMapper {
    long countByExample(CfgPairExample example);

    int deleteByExample(CfgPairExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgPair record);

    int insertSelective(CfgPair record);

    List<CfgPair> selectByExampleWithRowbounds(CfgPairExample example, RowBounds rowBounds);

    List<CfgPair> selectByExample(CfgPairExample example);

    CfgPair selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgPair record, @Param("example") CfgPairExample example);

    int updateByExample(@Param("record") CfgPair record, @Param("example") CfgPairExample example);

    int updateByPrimaryKeySelective(CfgPair record);

    int updateByPrimaryKey(CfgPair record);
}