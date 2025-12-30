package com.anyi.common.exchange.service;

import com.anyi.common.exchange.domain.OcpxNotify;
import com.anyi.common.exchange.mapper.OcpxNotifyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/30
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OcpxNotifyService extends ServiceImpl<OcpxNotifyMapper, OcpxNotify> {

    @Async
    public void addLog(String excId, String action) {
        OcpxNotify notify = OcpxNotify.builder()
                .excId(excId)
                .action(action)
                .build();
        this.save(notify);
    }
}