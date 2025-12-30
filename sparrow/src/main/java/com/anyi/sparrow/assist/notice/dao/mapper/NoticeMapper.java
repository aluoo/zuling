package com.anyi.sparrow.assist.notice.dao.mapper;

import com.anyi.sparrow.assist.notice.domain.Notice;
import com.anyi.sparrow.assist.notice.domain.NoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface NoticeMapper {
    long countByExample(NoticeExample example);

    int deleteByExample(NoticeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Notice record);

    int insertSelective(Notice record);

    List<Notice> selectByExampleWithRowbounds(NoticeExample example, RowBounds rowBounds);

    List<Notice> selectByExample(NoticeExample example);

    Notice selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Notice record, @Param("example") NoticeExample example);

    int updateByExample(@Param("record") Notice record, @Param("example") NoticeExample example);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);
}