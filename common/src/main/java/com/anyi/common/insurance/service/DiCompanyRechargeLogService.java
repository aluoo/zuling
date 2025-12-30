package com.anyi.common.insurance.service;

import com.anyi.common.account.req.AccountRechargeReq;
import com.anyi.common.company.domain.RecycleRechargeLog;
import com.anyi.common.insurance.domain.DiCompanyRechargeLog;
import com.anyi.common.insurance.mapper.DiCompanyRechargeLogMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数保门店账户充值记录表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiCompanyRechargeLogService extends ServiceImpl<DiCompanyRechargeLogMapper, DiCompanyRechargeLog>  {

    public void saveRecharge(AccountRechargeReq req) {
        DiCompanyRechargeLog log = new DiCompanyRechargeLog();
        log.setCompanyId(req.getCompanyId());
        log.setRechargeAmount(req.getRechargeAmount() * 100);
        log.setStatus(0);
        log.setImageUrl(req.getImageUrl());
        this.save(log);
    }

    public List<DiCompanyRechargeLog> rechargeList(Long companyId){
        return this.list(Wrappers.lambdaQuery(DiCompanyRechargeLog.class)
                .eq(DiCompanyRechargeLog::getCompanyId,companyId));
    }

}
