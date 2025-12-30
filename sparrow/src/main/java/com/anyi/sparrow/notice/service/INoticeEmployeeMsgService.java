package com.anyi.sparrow.notice.service;

import com.anyi.sparrow.notice.domain.NoticeEmployeeMsg;
import com.anyi.sparrow.notice.dto.NoticeUserMsgDTO;
import com.anyi.sparrow.notice.dto.UnReadMsgDTO;
import com.anyi.common.notice.domain.enums.MsgBizTypeEnum;
import com.anyi.sparrow.notice.req.NoticeListReq;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知_员工消息 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
public interface INoticeEmployeeMsgService extends IService<NoticeEmployeeMsg> {

    /**
     * 获取最近一条未读消息接口
     *
     * @param employeeId 员工id
     * @return
     */
    UnReadMsgDTO findLastUnReadMsg(Long employeeId);

    /**
     * 消息置为已读
     *
     * @param employeeId 员工id
     * @param msgId      消息ID
     */
    void changeMsgRead(Long employeeId, Long msgId);

    /***
     * 首页系统重要公告弹窗
     * @param employeeId
     * @return
     */
    List<NoticeUserMsgDTO> getIndexSysNocite(Long employeeId);

    /***
     * 首页系统重要公告轮播
     * @param employeeId
     * @return
     */
    List<NoticeUserMsgDTO> getSysNociteSlide(Long employeeId);

    /***
     * 公告列表
     * @param req
     * @return
     */
    List<NoticeUserMsgDTO> getList(NoticeListReq req);

    /***
     * 公告详情
     * @param employeeId
     * @return
     */
    NoticeUserMsgDTO noticeDetail(Long msgId, Long employeeId);

    /**
     * 获取最近一条未读消息接口
     *
     * @param employeeId 员工id
     * @return
     */
    Map getLastSysUnReadMsg(Long employeeId);

    /**
     * 插入一条消息
     *
     * @param employeeId
     * @param type
     * @param title
     * @param content
     * @param bizId
     */
    void addMessage(Long employeeId, MsgBizTypeEnum type, String title, String content, Long bizId);
}