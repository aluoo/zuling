package com.anyi.sparrow.assist.system.dao.mapper;

import com.anyi.sparrow.assist.system.domain.ImgConfig;
import com.anyi.sparrow.assist.system.domain.ImgConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ImgConfigMapper {
    long countByExample(ImgConfigExample example);

    int deleteByExample(ImgConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ImgConfig record);

    int insertSelective(ImgConfig record);

    List<ImgConfig> selectByExampleWithRowbounds(ImgConfigExample example, RowBounds rowBounds);

    List<ImgConfig> selectByExample(ImgConfigExample example);

    ImgConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ImgConfig record, @Param("example") ImgConfigExample example);

    int updateByExample(@Param("record") ImgConfig record, @Param("example") ImgConfigExample example);

    int updateByPrimaryKeySelective(ImgConfig record);

    int updateByPrimaryKey(ImgConfig record);
}