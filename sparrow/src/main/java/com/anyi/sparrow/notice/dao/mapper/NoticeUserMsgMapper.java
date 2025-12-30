package com.anyi.sparrow.notice.dao.mapper;

import com.anyi.sparrow.notice.domain.NoticeUserMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Repository
@Mapper
public interface NoticeUserMsgMapper extends BaseMapper<NoticeUserMsg> {
}