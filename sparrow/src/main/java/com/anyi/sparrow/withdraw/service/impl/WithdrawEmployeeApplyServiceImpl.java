package com.anyi.sparrow.withdraw.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.anyi.sparrow.common.utils.DateUtils;
import com.anyi.common.withdraw.domain.enums.WithdrawApplyStatusEnum;
import com.anyi.sparrow.withdraw.dao.mapper.WithdrawEmployeeApplyMapper;
import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeApply;
import com.anyi.sparrow.withdraw.service.IWithdrawEmployeeApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 提现申请表 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Service
public class WithdrawEmployeeApplyServiceImpl extends ServiceImpl<WithdrawEmployeeApplyMapper, WithdrawEmployeeApply> implements IWithdrawEmployeeApplyService {

    @Override
    public Long queryMonthAccWithdrawAmountByAccountNo(Long employeeId, String accountNo) {

//        this.lambdaQuery().eq(WithdrawEmployeeApply::getEmployeeId,employeeId)
//                .gt(WithdrawEmployeeApply::getUpdateTime, LocalDateTimeUtil.of(startDate))
//                .lt(WithdrawEmployeeApply::getUpdateTime,LocalDateTimeUtil.of(stopDate))
//                .eq(WithdrawEmployeeApply::getStatus, WithdrawApplyStatusEnum.pay_success.getType())
        Date now = new Date();

        String startMonthDay = DateUtils.dateToStr(now, "yyyy-MM");
        Date startDate = DateUtils.strToDate(startMonthDay, "yyyy-MM");

        Date stopDate = DateUtils.adjustMonth(startDate, 1);
        Long accWithdrawAmount = this.getBaseMapper().queryAccWithdrawAmountByAccountNo(employeeId, accountNo, startDate, stopDate, WithdrawApplyStatusEnum.pay_success.getType());
        return accWithdrawAmount;
    }

    @Override
    public Long queryTodayApplyTimes(Long employeeId) {
        Date now = new Date();
        String startMonthDay = DateUtils.dateToStr(now, "yyyy-MM-dd");
        Date startDate = DateUtils.strToDate(startMonthDay, "yyyy-MM-dd");

        Date stopDate = DateUtils.adjustDay(startDate, 1);

        Long count = this.lambdaQuery()
                .eq(WithdrawEmployeeApply::getEmployeeId, employeeId)
                .ge(WithdrawEmployeeApply::getCreateTime, LocalDateTimeUtil.of(startDate))
                .le(WithdrawEmployeeApply::getCreateTime, LocalDateTimeUtil.of(stopDate))
                .ne(WithdrawEmployeeApply::getStatus, WithdrawApplyStatusEnum.fail.getType())
                .count();

        return count == null ? 0L : count;
    }

    @Override
    public Long queryMonthAccAmountCommited(Long employeeId, String accountNo) {

        Date now = new Date();
        String startMonthDay = DateUtils.dateToStr(now, "yyyy-MM");
        Date startDate = DateUtils.strToDate(startMonthDay, "yyyy-MM");

        Date stopDate = DateUtils.adjustMonth(startDate, 1);
        Long accWithdrawAmount = this.getBaseMapper().queryAccWithdrawAmountCommitted(employeeId, accountNo, startDate, stopDate);
        ;
        return accWithdrawAmount;
    }
}