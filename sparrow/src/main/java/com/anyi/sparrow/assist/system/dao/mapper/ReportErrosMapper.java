package com.anyi.sparrow.assist.system.dao.mapper;

import com.anyi.sparrow.assist.system.domain.ReportErros;
import com.anyi.sparrow.assist.system.domain.ReportErrosExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReportErrosMapper {
    long countByExample(ReportErrosExample example);

    int deleteByExample(ReportErrosExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ReportErros record);

    int insertSelective(ReportErros record);

    List<ReportErros> selectByExampleWithRowbounds(ReportErrosExample example, RowBounds rowBounds);

    List<ReportErros> selectByExample(ReportErrosExample example);

    ReportErros selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ReportErros record, @Param("example") ReportErrosExample example);

    int updateByExample(@Param("record") ReportErros record, @Param("example") ReportErrosExample example);

    int updateByPrimaryKeySelective(ReportErros record);

    int updateByPrimaryKey(ReportErros record);
}