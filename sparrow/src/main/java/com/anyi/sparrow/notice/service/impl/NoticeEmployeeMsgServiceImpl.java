package com.anyi.sparrow.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.notice.dao.mapper.NoticeEmployeeMsgMapper;
import com.anyi.sparrow.notice.domain.NoticeEmployeeMsg;
import com.anyi.sparrow.notice.domain.SysContent;
import com.anyi.sparrow.notice.dto.NoticeUserMsgDTO;
import com.anyi.sparrow.notice.dto.UnReadMsgDTO;
import com.anyi.common.notice.domain.enums.MsgBizTypeEnum;
import com.anyi.common.notice.domain.enums.MsgReadStatusEnum;
import com.anyi.sparrow.notice.enums.SysContentStatusEnum;
import com.anyi.sparrow.notice.enums.SysContentTypeEnum;
import com.anyi.sparrow.notice.req.NoticeListReq;
import com.anyi.sparrow.notice.service.INoticeEmployeeMsgService;
import com.anyi.sparrow.notice.service.ISysContentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 通知_员工消息 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-23
 */
@Service
public class NoticeEmployeeMsgServiceImpl extends ServiceImpl<NoticeEmployeeMsgMapper, NoticeEmployeeMsg> implements INoticeEmployeeMsgService {

    @Autowired
    ISysContentService sysContentService;

    @Override
    public UnReadMsgDTO findLastUnReadMsg(Long employeeId) {
        UnReadMsgDTO dto = this.getBaseMapper().findLastUnReadMsg(employeeId, "withdraw");
        return dto;
    }

    @Override
    public void changeMsgRead(Long employeeId, Long msgId) {

        NoticeEmployeeMsg msg = this.lambdaQuery()
                .eq(NoticeEmployeeMsg::getId, msgId)
                .eq(NoticeEmployeeMsg::getEmployeeId, employeeId).one();

        if (msg == null) {
            throw new BusinessException(BizError.PARAM_ERROR);
        }
        NoticeEmployeeMsg updateMsg = new NoticeEmployeeMsg();
        updateMsg.setId(msg.getId());
        updateMsg.setHasRead(Boolean.TRUE);
        updateMsg.setReadTime(new Date());
        updateMsg.setUpdateTime(updateMsg.getReadTime());
        this.getBaseMapper().updateById(updateMsg);
    }


    @Override
    public List<NoticeUserMsgDTO> getIndexSysNocite(Long employeeId) {
        List<NoticeUserMsgDTO> resultList = new ArrayList<>();
        //当前时间-48小时
        LocalDateTime minTime = LocalDateTime.now().minusDays(2);
        //获取重要系统公告ID
        LambdaQueryWrapper<SysContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysContent::getType, SysContentTypeEnum.IMPORTANCE.getCode());
        queryWrapper.eq(SysContent::getStatus, SysContentStatusEnum.PUBLISH.getCode());
        List<SysContent> contentList = sysContentService.list(queryWrapper);
        if (CollUtil.isEmpty(contentList)) {
            return resultList;
        }
        //时间范围内的未读重要公告
        List<Long> ids = contentList.stream().map(SysContent::getId).collect(Collectors.toList());
        LambdaQueryWrapper<NoticeEmployeeMsg> msgWrapper = new LambdaQueryWrapper<>();
        msgWrapper.in(NoticeEmployeeMsg::getBizId, ids);
        msgWrapper.eq(NoticeEmployeeMsg::getEmployeeId, employeeId);
        msgWrapper.eq(NoticeEmployeeMsg::getHasRead, MsgReadStatusEnum.UNREAD.getCode());
        msgWrapper.ge(NoticeEmployeeMsg::getPushTime, minTime);
        msgWrapper.orderByDesc(NoticeEmployeeMsg::getCreateTime);
        List<NoticeEmployeeMsg> msgList = this.list(msgWrapper);
        if (CollUtil.isEmpty(msgList)) {
            return resultList;
        }
        return BeanUtil.copyToList(msgList, NoticeUserMsgDTO.class);

    }

    @Override
    public List<NoticeUserMsgDTO> getSysNociteSlide(Long employeeId) {
        List<NoticeUserMsgDTO> resultList = new ArrayList<>();
        //当前时间-48小时
        LocalDateTime minTime = LocalDateTime.now().minusDays(3);
        //获取重要系统公告ID
        LambdaQueryWrapper<SysContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysContent::getType, SysContentTypeEnum.IMPORTANCE.getCode());
        queryWrapper.eq(SysContent::getStatus, SysContentStatusEnum.PUBLISH.getCode());
        List<SysContent> contentList = sysContentService.list(queryWrapper);
        if (CollUtil.isEmpty(contentList)) {
            return resultList;
        }
        //时间范围内的重要公告
        List<Long> ids = contentList.stream().map(SysContent::getId).collect(Collectors.toList());
        LambdaQueryWrapper<NoticeEmployeeMsg> msgWrapper = new LambdaQueryWrapper<>();
        msgWrapper.in(NoticeEmployeeMsg::getBizId, ids);
        msgWrapper.eq(NoticeEmployeeMsg::getEmployeeId, employeeId);
        msgWrapper.ge(NoticeEmployeeMsg::getPushTime, minTime);
        msgWrapper.orderByDesc(NoticeEmployeeMsg::getCreateTime);
        List<NoticeEmployeeMsg> msgList = this.list(msgWrapper);
        if (CollUtil.isEmpty(msgList)) {
            return resultList;
        }
        return BeanUtil.copyToList(msgList, NoticeUserMsgDTO.class);
    }

    @Override
    public List<NoticeUserMsgDTO> getList(NoticeListReq req) {
        List<NoticeUserMsgDTO> resultList = new ArrayList<>();
        LambdaQueryWrapper<NoticeEmployeeMsg> msgWrapper = new LambdaQueryWrapper<>();
        msgWrapper.eq(NoticeEmployeeMsg::getEmployeeId, req.getEmployeeId());
        if (StrUtil.isNotBlank(req.getBizType())) {
            msgWrapper.in(NoticeEmployeeMsg::getBizType, Arrays.asList(req.getBizType().split(",")));
        }
        msgWrapper.orderByDesc(NoticeEmployeeMsg::getCreateTime);
        PageHelper.startPage(req.getPage(), req.getPageSize());
        List<NoticeEmployeeMsg> msgList = this.list(msgWrapper);
        if (CollUtil.isEmpty(msgList)) {
            return resultList;
        }
        resultList = BeanUtil.copyToList(msgList, NoticeUserMsgDTO.class);
        //获取重要系统公告ID
        LambdaQueryWrapper<SysContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysContent::getType, SysContentTypeEnum.IMPORTANCE.getCode());
        queryWrapper.eq(SysContent::getStatus, SysContentStatusEnum.PUBLISH.getCode());
        List<SysContent> contentList = sysContentService.list(queryWrapper);
        if (CollUtil.isNotEmpty(contentList)) {
            List<Long> importIds = contentList.stream().map(SysContent::getId).collect(Collectors.toList());
            for (NoticeUserMsgDTO msg : resultList) {
                //判断重要公告
                if (importIds.contains(msg.getBizId())) {
                    msg.setImportFlag(true);
                }
            }
        }
        for (NoticeUserMsgDTO msg : resultList) {
            msg.setTypeName(MsgBizTypeEnum.getTypeByCode(msg.getBizType()));
        }
        return resultList;
    }

    @Override
    public NoticeUserMsgDTO noticeDetail(Long msgId, Long employeeId) {
        NoticeUserMsgDTO resultVo = new NoticeUserMsgDTO();
        NoticeEmployeeMsg msg = this.lambdaQuery()
                .eq(NoticeEmployeeMsg::getId, msgId)
                .eq(NoticeEmployeeMsg::getEmployeeId, employeeId).one();
        if (msg == null) {
            return resultVo;
        }

        SysContent content = sysContentService.getById(msg.getBizId());
        if (content.getType().equals(SysContentTypeEnum.IMPORTANCE.getCode())) {
            resultVo.setImportFlag(true);
        } else {
            resultVo.setImportFlag(false);
        }

        BeanUtil.copyProperties(msg, resultVo);
        return resultVo;
    }

    @Override
    public Map getLastSysUnReadMsg(Long employeeId) {
        LambdaQueryWrapper<NoticeEmployeeMsg> msgWrapper = new LambdaQueryWrapper<>();
        msgWrapper.eq(NoticeEmployeeMsg::getEmployeeId, employeeId);
        msgWrapper.eq(NoticeEmployeeMsg::getHasRead, false);
        msgWrapper.orderByDesc(NoticeEmployeeMsg::getCreateTime);
        List<NoticeEmployeeMsg> msgList = this.list(msgWrapper);
        Map<String, Object> resultMap = new HashMap<>();
        if (CollUtil.isEmpty(msgList)) {
            resultMap.put("unReadNum", 0);
            return resultMap;
        }
        resultMap.put("unReadNum", msgList.size());
        return resultMap;
    }

    @Override
    public void addMessage(Long employeeId, MsgBizTypeEnum type, String title, String content, Long bizId) {
        NoticeEmployeeMsg msg = new NoticeEmployeeMsg();
        msg.setEmployeeId(employeeId);
        msg.setBizType(type.getCode());
        msg.setTitle(title);
        msg.setDigest(content);
        msg.setContent(content);
        msg.setHasRead(false);
        msg.setBizId(bizId);
        msg.setCreateTime(new Date());
        this.save(msg);
    }

}