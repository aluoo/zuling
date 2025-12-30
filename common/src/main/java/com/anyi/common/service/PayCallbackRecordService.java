package com.anyi.common.service;

import com.anyi.common.domain.entity.PayCallbackRecord;
import com.anyi.common.domain.mapper.PayCallbackRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.ecommerce.PartnerTransactionsNotifyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class PayCallbackRecordService extends ServiceImpl<PayCallbackRecordMapper, PayCallbackRecord> {
    @Autowired
    private ProductWxPayService productWxPayService;

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(String payEnter, String mchId, String data) {
        PartnerTransactionsNotifyResult res = productWxPayService.parseNotify(data, mchId);

        PayCallbackRecord record = new PayCallbackRecord();
        record.setCreateTime(new Date());
        record.setActualMchid(mchId);
        record.setOriginalInfo(data);
        record.setPayEnter(payEnter);
        record.setDecryptedInfo(res != null ? res.toString() : null);
        record.setOutTradeNo(res != null ? res.getResult().getOutTradeNo() : null);
        this.save(record);
    }
}