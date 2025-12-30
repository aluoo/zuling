package com.anyi.common.exchange.service;

import com.anyi.common.exchange.domain.WyNotify;
import com.anyi.common.exchange.mapper.WyNotifyMapper;
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
public class WyNotifyService extends ServiceImpl<WyNotifyMapper, WyNotify> {

    @Async
    public void addLog(String excId, String action) {
        WyNotify notify = WyNotify.builder()
                .excId(excId)
                .action(action)
                .build();
        this.save(notify);
    }
}