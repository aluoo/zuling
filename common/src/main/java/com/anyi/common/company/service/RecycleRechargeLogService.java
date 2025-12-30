package com.anyi.common.company.service;

import com.anyi.common.account.req.AccountRechargeReq;
import com.anyi.common.company.domain.RecycleRechargeLog;
import com.anyi.common.company.mapper.RecycleRechargeLogMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class RecycleRechargeLogService extends ServiceImpl<RecycleRechargeLogMapper, RecycleRechargeLog> {

    public void saveRecharge(AccountRechargeReq req) {
        RecycleRechargeLog log = new RecycleRechargeLog();
        log.setCompanyId(req.getCompanyId());
        log.setRechargeAmount(req.getRechargeAmount() * 100);
        log.setStatus(0);
        log.setImageUrl(req.getImageUrl());
        this.save(log);
    }

}