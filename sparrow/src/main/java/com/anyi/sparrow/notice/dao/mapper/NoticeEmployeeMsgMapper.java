package com.anyi.sparrow.notice.dao.mapper;


import com.anyi.sparrow.notice.domain.NoticeEmployeeMsg;
import com.anyi.sparrow.notice.dto.UnReadMsgDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 通知_员工消息 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
public interface NoticeEmployeeMsgMapper extends BaseMapper<NoticeEmployeeMsg> {

    UnReadMsgDTO findLastUnReadMsg(@Param("employeeId") Long employeeId, @Param("bizType") String bizType);
}
