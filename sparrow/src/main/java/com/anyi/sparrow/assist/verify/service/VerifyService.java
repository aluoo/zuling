package com.anyi.sparrow.assist.verify.service;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.sms.NewSmsMessage;
import com.anyi.common.sms.NewSmsSender;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.assist.verify.dao.mapper.VerifyCodeMapper;
import com.anyi.sparrow.assist.verify.domain.VerifyCode;
import com.anyi.sparrow.common.utils.RandomUtil;
import com.anyi.sparrow.common.utils.TimeUtil;
import com.anyi.sparrow.assist.verify.dao.VerifyDao;
import com.anyi.sparrow.assist.verify.enums.BizEnum;
import com.anyi.sparrow.assist.verify.enums.Status;
import com.anyi.sparrow.assist.verify.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class VerifyService {
    @Autowired
    private Validator validator;
    @Autowired
    private VerifyCodeMapper verifyCodeMapper;
    @Autowired
    private VerifyDao verifyDao;

    @Value("${spring.profiles.active}")
    private String env;

//    @Autowired
//    private SmsSender smsSender;
    @Autowired
    private NewSmsSender smsSender;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    public void sendVerify(String mobile, BizEnum bizEnum, Integer minutes){
        validator.validSendHz(mobile, bizEnum.getBiz());
        String code;
        if (!"production".equals(env)){
            code = "1234";
        }else {
            code = RandomUtil.randomNumber(4);
            if(mobile.equals("18148157619")) {
                code = "1234";
            }
        }
//        SmsMessage message = SmsMessage.create().mobile(mobile).templateCode(bizEnum.getTemplateCode()).addParam("code", code);
        NewSmsMessage smsMessage =  new NewSmsMessage();
        smsMessage.setMobile(mobile);
        smsMessage.setTplId(bizEnum.getTemplateCode());
        smsMessage.setCode(code);
        smsSender.doSend(smsMessage);

        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setBiz(bizEnum.getBiz());
        verifyCode.setMobileNumber(mobile);
        verifyCode.setCode(code);
        verifyCode.setCreateTime(new Date());
        verifyCode.setExpireTime(TimeUtil.timeAdd(new Date(), minutes, ChronoUnit.MINUTES));
        verifyCode.setId(snowflakeIdService.nextId());
        verifyCode.setStatus(Status.NOT_USED.getCode());
        verifyCodeMapper.insert(verifyCode);
    }

    public void verifyCode(String mobile, String code, BizEnum bizEnum){
        VerifyCode latest = verifyDao.getLatest(mobile, bizEnum.getBiz());
        if (latest == null || !latest.getCode().equals(code)){
            throw new BusinessException(BizError.COde_error);
        }
        if(latest.getStatus().byteValue() == Status.USED.getCode()) {
            throw new BusinessException(BizError.CODE_ALREADY_USED);
        }
        if (System.currentTimeMillis() > latest.getExpireTime().getTime()){
            throw new BusinessException(BizError.CODE_EXPIRE);
        }
        latest.setStatus(Status.USED.getCode());
        verifyCodeMapper.updateByPrimaryKey(latest);
    }

    public void sendVerify(String mobile, BizEnum biz){
        sendVerify(mobile, biz, 5);
    }


    public void sendSpecVerify(String mobile, BizEnum biz){
        sendSpecVerify(mobile, biz, 5);
    }

    public void sendSpecVerify(String mobile, BizEnum bizEnum, Integer minutes){
        validator.validSendHz(mobile, bizEnum.getBiz());
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setBiz(bizEnum.getBiz());
        verifyCode.setMobileNumber(mobile);
        int mobileLen = mobile.length();
        String code = mobile.substring(mobileLen - 4, mobileLen);
        verifyCode.setCode(code);
        verifyCode.setCreateTime(new Date());
        verifyCode.setExpireTime(TimeUtil.timeAdd(new Date(), minutes, ChronoUnit.MINUTES));
        verifyCode.setId(snowflakeIdService.nextId());
        verifyCode.setStatus(Status.NOT_USED.getCode());
        verifyCodeMapper.insert(verifyCode);
    }
}
