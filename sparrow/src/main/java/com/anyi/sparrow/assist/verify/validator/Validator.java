package com.anyi.sparrow.assist.verify.validator;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.sparrow.assist.verify.domain.VerifyCode;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.assist.verify.dao.VerifyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Validator {
    @Autowired
    private VerifyDao verifyDao;
    @Value("${verify.validator.minHz:3}")

    private Integer minHz;
    @Value("${verify.validator.dayHz:100}")
    private Integer dayHz;
    @Autowired
    private SysDictService dictService;

    public void validSendHz(String mobile, String biz) {

        VerifyCode verifyCode = verifyDao.getLatest(mobile, biz);
        if (verifyCode == null){
            return;
        }
        if (System.currentTimeMillis() - verifyCode.getCreateTime().getTime() <= minHz * 1000){
            throw new BusinessException(BizError.SMS_TOO_frequency);
        }
        if (System.currentTimeMillis() - verifyCode.getCreateTime().getTime() <= minHz * 1000){
            throw new BusinessException(BizError.SMS_TOO_frequency);
        }
        Integer times = verifyDao.getTodayTimes(mobile, biz);
        if (times >= dayHz){
            throw new BusinessException(BizError.OUT_DAY_SEND_TIMES);
        }
    }
}
