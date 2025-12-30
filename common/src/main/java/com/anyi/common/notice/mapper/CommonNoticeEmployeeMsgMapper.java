package com.anyi.common.notice.mapper;

import com.anyi.common.notice.domain.CommonNoticeEmployeeMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/22
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface CommonNoticeEmployeeMsgMapper extends BaseMapper<CommonNoticeEmployeeMsg> {
}