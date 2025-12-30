package com.anyi.sparrow.assist.projects.dao.mapper;


import com.anyi.sparrow.assist.projects.domain.SysProjects;
import com.anyi.sparrow.assist.projects.domain.SysProjectsExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import java.util.List;

public interface SysProjectsMapper {
    long countByExample(SysProjectsExample example);

    int deleteByExample(SysProjectsExample example);

    int deleteByPrimaryKey(Integer projectId);

    int insert(SysProjects record);

    int insertSelective(SysProjects record);

    List<SysProjects> selectByExampleWithBLOBsWithRowbounds(SysProjectsExample example, RowBounds rowBounds);

    List<SysProjects> selectByExampleWithBLOBs(SysProjectsExample example);

    List<SysProjects> selectByExampleWithRowbounds(SysProjectsExample example, RowBounds rowBounds);

    List<SysProjects> selectByExample(SysProjectsExample example);

    SysProjects selectByPrimaryKey(Integer projectId);

    int updateByExampleSelective(@Param("record") SysProjects record, @Param("example") SysProjectsExample example);

    int updateByExampleWithBLOBs(@Param("record") SysProjects record, @Param("example") SysProjectsExample example);

    int updateByExample(@Param("record") SysProjects record, @Param("example") SysProjectsExample example);

    int updateByPrimaryKeySelective(SysProjects record);

    int updateByPrimaryKeyWithBLOBs(SysProjects record);

    int updateByPrimaryKey(SysProjects record);
}