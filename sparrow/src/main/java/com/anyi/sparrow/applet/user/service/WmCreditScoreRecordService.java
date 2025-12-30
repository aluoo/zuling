package com.anyi.sparrow.applet.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anyi.sparrow.applet.enums.CreditState;
import com.anyi.sparrow.applet.enums.CreditTypeEnum;
import com.anyi.sparrow.applet.user.domain.WmCreditScoreRecord;
import com.anyi.sparrow.applet.user.mapper.WmCreditScoreRecordMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author shenbinhong
 * @date 2023-05-16
 */
@Service
public class WmCreditScoreRecordService extends ServiceImpl<WmCreditScoreRecordMapper, WmCreditScoreRecord> {

    /**
     * 积分记录
     *  @param userId  用户id
     * @param etcNo   etc卡号
     * @param state   添加或者减少积分
     * @param content 积分内容
     * @param score 变动积分值
     * @param originalScore
     * @param score
     */
    public void addRecord(Long userId, String etcNo, CreditState state, String content, Long score, Long originalScore, Long resultScore,String plateNumber) {
        WmCreditScoreRecord record = new WmCreditScoreRecord();
        record.setUserId(userId);
        record.setEtcNo(etcNo);
        record.setState(state.getState());
        record.setContent(content);
        record.setChangeScore(score);

        CreditTypeEnum typeEnum = state == CreditState.REDUCE ? CreditTypeEnum.REFUND : CreditTypeEnum.REGIST_ETC;
        record.setType(typeEnum.getCode());
        record.setBeforeScore(originalScore);
        record.setAfterScore(resultScore);
        record.setPlateNumber(plateNumber);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        this.save(record);
    }

    public void updateEtcNo(Long userId, String plateNumber, String etcNo) {
        WmCreditScoreRecord credit = new WmCreditScoreRecord();
        credit.setEtcNo(etcNo);
        credit.setUpdateTime(LocalDateTime.now());
        this.lambdaUpdate().eq(WmCreditScoreRecord::getUserId, userId).eq(WmCreditScoreRecord::getPlateNumber, plateNumber).update(credit);
    }

    public void updateEtcNoForNew(Long userId,String originalPlateNumber, String plateNumber, String etcNo) {
        WmCreditScoreRecord credit = new WmCreditScoreRecord();
        credit.setEtcNo(etcNo);
        credit.setUpdateTime(LocalDateTime.now());
        if (originalPlateNumber.equals(plateNumber)){
            this.lambdaUpdate().eq(WmCreditScoreRecord::getUserId, userId).eq(WmCreditScoreRecord::getPlateNumber, plateNumber).update(credit);
        }else{
            credit.setPlateNumber(plateNumber);
            this.lambdaUpdate().eq(WmCreditScoreRecord::getUserId, userId).eq(WmCreditScoreRecord::getPlateNumber, originalPlateNumber).update(credit);

        }
    }
}
