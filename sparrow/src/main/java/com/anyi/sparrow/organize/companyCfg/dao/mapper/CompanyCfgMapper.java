package com.anyi.sparrow.organize.companyCfg.dao.mapper;

import com.anyi.sparrow.organize.companyCfg.domain.CompanyCfg;
import com.anyi.sparrow.organize.companyCfg.domain.CompanyCfgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CompanyCfgMapper {
    long countByExample(CompanyCfgExample example);

    int deleteByExample(CompanyCfgExample example);

    int insert(CompanyCfg record);

    int insertSelective(CompanyCfg record);

    List<CompanyCfg> selectByExampleWithRowbounds(CompanyCfgExample example, RowBounds rowBounds);

    List<CompanyCfg> selectByExample(CompanyCfgExample example);

    int updateByExampleSelective(@Param("record") CompanyCfg record, @Param("example") CompanyCfgExample example);

    int updateByExample(@Param("record") CompanyCfg record, @Param("example") CompanyCfgExample example);
}