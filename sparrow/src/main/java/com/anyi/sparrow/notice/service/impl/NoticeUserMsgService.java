package com.anyi.sparrow.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anyi.sparrow.notice.dao.mapper.NoticeUserMsgMapper;
import com.anyi.sparrow.notice.domain.NoticeUserMsg;
import com.anyi.sparrow.notice.dto.NoticeUserMsgDTO;
import com.anyi.common.notice.domain.enums.MsgReadStatusEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/5/13
 */
@Slf4j
@Service
public class NoticeUserMsgService extends ServiceImpl<NoticeUserMsgMapper, NoticeUserMsg> {

    @Transactional(rollbackFor = Exception.class)
    public void send(NoticeUserMsgDTO req) {
        if (req == null) {
            return;
        }
        NoticeUserMsg bean = BeanUtil.copyProperties(req, NoticeUserMsg.class);
        bean.setCreateTime(LocalDateTime.now());
        this.save(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendBatch(List<NoticeUserMsgDTO> req) {
        if (CollUtil.isEmpty(req)) {
            return;
        }
        List<NoticeUserMsg> list = BeanUtil.copyToList(req, NoticeUserMsg.class);
        list.forEach(o -> o.setCreateTime(LocalDateTime.now()));
        this.saveBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void read(Long id, Long userId) {
        if (id == null || userId == null) {
            return;
        }
        log.info("update");
        lambdaUpdate()
                .set(NoticeUserMsg::getHasRead, MsgReadStatusEnum.READ.getCode())
                .set(NoticeUserMsg::getReadTime, LocalDateTime.now())
                .set(NoticeUserMsg::getUpdateTime, LocalDateTime.now())
                .eq(NoticeUserMsg::getId, id)
                .eq(NoticeUserMsg::getUserId, userId)
                .eq(NoticeUserMsg::getHasRead, MsgReadStatusEnum.UNREAD.getCode())
                .update();
    }

    @Transactional(rollbackFor = Exception.class)
    public void readBatch(List<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids) || userId == null) {
            return;
        }
        lambdaUpdate()
                .set(NoticeUserMsg::getHasRead, MsgReadStatusEnum.READ.getCode())
                .set(NoticeUserMsg::getReadTime, LocalDateTime.now())
                .set(NoticeUserMsg::getUpdateTime, LocalDateTime.now())
                .in(NoticeUserMsg::getId, ids)
                .eq(NoticeUserMsg::getUserId, userId)
                .eq(NoticeUserMsg::getHasRead, MsgReadStatusEnum.UNREAD.getCode())
                .update();
    }

}