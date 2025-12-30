package com.anyi.sparrow.applet.user.service;

import cn.hutool.core.util.StrUtil;
import com.anyi.sparrow.applet.user.constant.WmCreditScoreChangeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.anyi.sparrow.applet.user.domain.WmCreditScore;
import com.anyi.sparrow.applet.user.domain.WmCreditScoreRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
public class WmCreditScoreChangeService {


    @Autowired
    private WmCreditScoreService wmCreditScoreService;

    @Autowired
    private WmCreditScoreRecordService creditScoreRecordService;

    @Transactional(rollbackFor = Exception.class)
    public void removeExist(Long wmUserId, String etcNo, String plateNumber) {
        WmCreditScore exist = wmCreditScoreService.lambdaQuery()
                .eq(WmCreditScore::getUserId, wmUserId)
                .eq(WmCreditScore::getPlateNumber, plateNumber)
                .one();
        if (exist == null) {
            return;
        }
        wmCreditScoreService.removeById(exist);
        creditScoreRecordService.remove(new LambdaQueryWrapper<WmCreditScoreRecord>()
                .eq(WmCreditScoreRecord::getUserId, wmUserId)
                .eq(StrUtil.isNotBlank(etcNo), WmCreditScoreRecord::getEtcNo, etcNo)
                .eq(WmCreditScoreRecord::getPlateNumber, plateNumber));
    }

    /**
     * 账户金币变更 统一入口方法
     *
     * @param wmUserId          员工id
     * @param accountChangeEnum 变更类型枚举类
     * @param changeBalance     变更账户值
     * @return
     */
    @Transactional
    public boolean changeScore(Long wmUserId,
                               String etcNo, String plateNumber,
                               WmCreditScoreChangeEnum accountChangeEnum, long changeBalance) {
        LambdaQueryChainWrapper<WmCreditScore> lambdaQueryWrapper = wmCreditScoreService.lambdaQuery();
        lambdaQueryWrapper =  lambdaQueryWrapper.eq(WmCreditScore::getUserId, wmUserId);
        if (Objects.isNull(etcNo)){
            lambdaQueryWrapper = lambdaQueryWrapper.isNull(WmCreditScore::getEtcNo);
        }else{
            lambdaQueryWrapper = lambdaQueryWrapper.eq(WmCreditScore::getEtcNo,etcNo);
        }
        lambdaQueryWrapper = lambdaQueryWrapper.eq(WmCreditScore::getPlateNumber, plateNumber);
//                .one();;

        WmCreditScore wmCreditScore2 =lambdaQueryWrapper.one();


        if (wmCreditScore2 == null) {
            wmCreditScore2 = new WmCreditScore();
            wmCreditScore2.setUserId(wmUserId);
            wmCreditScore2.setEtcNo(etcNo);
            wmCreditScore2.setScore(0L);
            wmCreditScore2.setTempScore(0L);
            wmCreditScore2.setPlateNumber(plateNumber);
            wmCreditScore2.setCreateTime(LocalDateTime.now());
            wmCreditScore2.setUpdateTime(LocalDateTime.now());
            wmCreditScoreService.save(wmCreditScore2);
        }

        WmCreditScore wmCreditScore = wmCreditScoreService.lambdaQuery().eq(WmCreditScore::getId, wmCreditScore2.getId()).last("for update").one();

        if (wmCreditScore == null) {
            return false;
        }

        long changeScore = changeBalance * accountChangeEnum.getChangeScore(),
                changeTempScore = changeBalance * accountChangeEnum.getChangeTempScore();


        long afterScore = wmCreditScore.getScore() + changeScore;
        if (afterScore > 10L) {
            // 最大只能为10分
            afterScore = 10L;
        }

        if (afterScore < 0L) {
            // 最小为0分
            afterScore = 0L;
        }


        long afterTempScore = wmCreditScore.getTempScore() + changeTempScore;


        WmCreditScoreRecord creditScoreRecord = new WmCreditScoreRecord();
        creditScoreRecord.setUserId(wmUserId);
        creditScoreRecord.setEtcNo(etcNo);
        creditScoreRecord.setPlateNumber(plateNumber);
        creditScoreRecord.setState(accountChangeEnum.getChangeState());
        creditScoreRecord.setRemark(accountChangeEnum.getRemark());
        creditScoreRecord.setContent(accountChangeEnum.getRemark());
        creditScoreRecord.setType(accountChangeEnum.getChangeType());

        creditScoreRecord.setChangeScore(changeBalance);

        creditScoreRecord.setBeforeScore(wmCreditScore.getScore());
        creditScoreRecord.setAfterScore(afterScore);
        creditScoreRecord.setBeforeTempScore(wmCreditScore.getTempScore());
        creditScoreRecord.setAfterTempScore(afterTempScore);


        creditScoreRecord.setCreateTime(LocalDateTime.now());
        creditScoreRecord.setUpdateTime(creditScoreRecord.getCreateTime());
        if (creditScoreRecordService.save(creditScoreRecord)) {

            WmCreditScore update = new WmCreditScore();
            update.setScore(creditScoreRecord.getAfterScore());
            update.setTempScore(creditScoreRecord.getAfterTempScore());
            update.setUpdateTime(LocalDateTime.now());

            boolean changeSuccess = wmCreditScoreService.lambdaUpdate()
                    .eq(WmCreditScore::getId, wmCreditScore.getId()).update(update);
            return changeSuccess;
        }
        return false;
    }


}