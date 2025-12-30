package com.anyi.sparrow.applet.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anyi.sparrow.applet.enums.CreditState;
import com.anyi.sparrow.applet.enums.CreditTypeEnum;
import com.anyi.sparrow.applet.user.domain.CreditRecord;
import com.anyi.sparrow.applet.user.mapper.CreditRecordMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author peng can
 * @date 2022/12/13
 */
@Service
public class CreditRecordService extends ServiceImpl<CreditRecordMapper, CreditRecord> {

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
        CreditRecord record = new CreditRecord();
        record.setUserId(userId);
        record.setEtcNo(etcNo);
        record.setState(state.getState());
        record.setContent(content);
        record.setTempScore(score);

        CreditTypeEnum typeEnum = state == CreditState.REDUCE ? CreditTypeEnum.REFUND : CreditTypeEnum.REGIST_ETC;
        record.setType(typeEnum.getCode());
        record.setOriginalScore(originalScore);
        record.setResultScore(resultScore);
        record.setPlateNumber(plateNumber);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        this.save(record);
    }

    public void updateEtcNo(Long userId, String plateNumber, String etcNo) {
        CreditRecord credit = new CreditRecord();
        credit.setEtcNo(etcNo);
        credit.setUpdateTime(LocalDateTime.now());
        this.lambdaUpdate().eq(CreditRecord::getUserId, userId).eq(CreditRecord::getPlateNumber, plateNumber).update(credit);
    }
}
