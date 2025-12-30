package com.anyi.sparrow.applet.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anyi.sparrow.applet.enums.CreditState;
import com.anyi.sparrow.applet.user.domain.Credit;
import com.anyi.sparrow.applet.user.mapper.CreditMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author peng can
 * @date 2022/12/13
 */
@Service
public class CreditService extends ServiceImpl<CreditMapper, Credit> {

    @Autowired
    private CreditRecordService creditRecordService;

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

        Credit credit = this.lambdaQuery().eq(Credit::getUserId, userId).eq(Credit::getPlateNumber, plateNumber).one();
        Long originalScore;
        if (Objects.isNull(credit)) {
            credit = new Credit();
            credit.setUserId(userId);
            credit.setEtcNo(etcNo);
            credit.setScore(score);
            credit.setPlateNumber(plateNumber);
            credit.setCreateTime(LocalDateTime.now());
            credit.setUpdateTime(LocalDateTime.now());
            this.save(credit);
            originalScore = 0L;
        } else {
            originalScore = credit.getScore();
            Long value = state == CreditState.REDUCE ? -score : score;
            this.baseMapper.editScore(credit.getId(), value, LocalDateTime.now());
        }
        Long resultScore = this.getById(credit.getId()).getScore();
        creditRecordService.addRecord(userId, etcNo, state, content, score, originalScore, resultScore, plateNumber);
    }

    public void updateEtcNo(Long userId, String plateNumber, String etcNo) {
        Credit credit = new Credit();
        credit.setEtcNo(etcNo);
        credit.setUpdateTime(LocalDateTime.now());
        this.lambdaUpdate().eq(Credit::getUserId, userId).eq(Credit::getPlateNumber, plateNumber).update(credit);

        creditRecordService.updateEtcNo(userId, plateNumber, etcNo);
    }
}
