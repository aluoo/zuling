package com.anyi.sparrow.notice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.common.utils.DateUtils;
import com.anyi.sparrow.notice.dto.NoticeUserMsgDTO;
import com.anyi.sparrow.notice.dto.NoticeUserMsgSendDTO;
import com.anyi.common.notice.domain.enums.MsgReadStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Slf4j
@Component
public class NoticeUserMsgSender {
    @Autowired
    private NoticeUserMsgService service;
    @Autowired
    private SysDictService sysDictService;

    public void send(NoticeUserMsgSendDTO req) {
        send(req.getTitle(), req.getContent(), req.getUserId(), req.getBizType(), req.getBizId(), req.getPushTime());
    }

    public void sendBatch(NoticeUserMsgSendDTO req) {
        sendBatch(req.getTitle(), req.getContent(), req.getUserIds(), req.getBizType(), req.getBizId(), req.getPushTime());
    }

    @Async
    public void sendAsync(NoticeUserMsgSendDTO req) {
        send(req.getTitle(), req.getContent(), req.getUserId(), req.getBizType(), req.getBizId(), req.getPushTime());
    }

    @Async
    public void sendBatchAsync(NoticeUserMsgSendDTO req) {
        sendBatch(req.getTitle(), req.getContent(), req.getUserIds(), req.getBizType(), req.getBizId(), req.getPushTime());
    }

    private void send(@NotNull String title, @NotNull String content, @NotNull Long userId, String bizType, Long bizId, LocalDateTime pushTime) {
        NoticeUserMsgDTO msg = NoticeUserMsgDTO.builder()
                .userId(userId)
                .bizType(bizType)
                .title(title)
                .content(content)
                .pushTime(pushTime != null ? DateUtils.strToDate(pushTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "yyyy-MM-dd HH:mm:ss") : new Date())
                .hasRead(MsgReadStatusEnum.UNREAD.getCode())
                .bizId(bizId)
                .build();
        log.info("发送用户消息通知: {}", JSONUtil.toJsonStr(msg));
        service.send(msg);
    }

    private void sendBatch(@NotNull String title, @NotNull String content, @NotNull List<Long> userIds, String bizType, Long bizId, LocalDateTime pushTime) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        List<NoticeUserMsgDTO> list = new ArrayList<>();
        userIds.forEach(userId -> {
            NoticeUserMsgDTO msg = NoticeUserMsgDTO.builder()
                    .userId(userId)
                    .bizType(bizType)
                    .title(title)
                    .content(content)
                    .pushTime(pushTime != null ? DateUtils.strToDate(pushTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "yyyy-MM-dd HH:mm:ss") : new Date())
                    .hasRead(MsgReadStatusEnum.UNREAD.getCode())
                    .bizId(bizId)
                    .build();
            list.add(msg);
        });
        service.sendBatch(list);
    }
}