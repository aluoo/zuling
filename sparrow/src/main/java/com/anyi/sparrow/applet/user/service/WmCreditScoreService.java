package com.anyi.sparrow.applet.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anyi.sparrow.applet.enums.CreditState;
import com.anyi.sparrow.applet.user.domain.WmCreditScore;
import com.anyi.sparrow.applet.user.mapper.WmCreditScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author shenbinhong
 * @date 2023-05-16
 */
@Service
public class WmCreditScoreService extends ServiceImpl<WmCreditScoreMapper, WmCreditScore> {

    @Autowired
    private WmCreditScoreRecordService creditRecordService;

    /**
     * 积分变动
     *
     * @param userId  用户id
     * @param etcNo   etc卡号
     * @param state   添加或者减少积分
     * @param content 积分内容
     * @param score   变动积分值
     */
    @Transactional(rollbackFor = Exception.class)
    public void editScore(Long userId, String etcNo, CreditState state, String content, Long score, String plateNumber) {

        WmCreditScore credit = this.lambdaQuery().eq(WmCreditScore::getUserId, userId).eq(WmCreditScore::getPlateNumber, plateNumber).one();
        Long originalScore;
        if (Objects.isNull(credit)) {
            credit = new WmCreditScore();
            credit.setUserId(userId);
            credit.setEtcNo(etcNo);
            credit.setScore(score);
            credit.setTempScore(0L);
            credit.setPlateNumber(plateNumber);
            credit.setCreateTime(LocalDateTime.now());
            credit.setUpdateTime(LocalDateTime.now());
            this.save(credit);
            originalScore = 0L;
        } else {
            originalScore = credit.getScore();
            Long value = state == CreditState.REDUCE ? -score : score;
            this.baseMapper.editScore(credit.getId(), value,0L, LocalDateTime.now());
        }
        Long resultScore = this.getById(credit.getId()).getScore();
        creditRecordService.addRecord(userId, etcNo, state, content, score, originalScore, resultScore, plateNumber);
    }

    public void updateEtcNo(Long userId, String plateNumber, String etcNo) {
        WmCreditScore credit = new WmCreditScore();
        credit.setEtcNo(etcNo);
        credit.setUpdateTime(LocalDateTime.now());
        this.lambdaUpdate().eq(WmCreditScore::getUserId, userId).eq(WmCreditScore::getPlateNumber, plateNumber).update(credit);

        creditRecordService.updateEtcNo(userId, plateNumber, etcNo);
    }

    public void updateEtcNoForNew(Long userId,String originalPlateNumber, String plateNumber, String etcNo) {
        WmCreditScore credit = new WmCreditScore();
        credit.setEtcNo(etcNo);
        credit.setUpdateTime(LocalDateTime.now());
        if (originalPlateNumber.equals(plateNumber)){
            this.lambdaUpdate().eq(WmCreditScore::getUserId, userId).eq(WmCreditScore::getPlateNumber, plateNumber).update(credit);
        }else{
            credit.setPlateNumber(plateNumber);
            this.lambdaUpdate().eq(WmCreditScore::getUserId, userId).eq(WmCreditScore::getPlateNumber, originalPlateNumber).update(credit);
        }


        creditRecordService.updateEtcNoForNew(userId,originalPlateNumber, plateNumber, etcNo);
    }
}
