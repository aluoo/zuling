package com.anyi.sparrow.withdraw.service;


import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeApply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 提现申请表 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface IWithdrawEmployeeApplyService extends IService<WithdrawEmployeeApply> {

    /**
     * 查询卡号本月的已提现到账金额
     *
     * @param employeeId
     * @param accountNo
     * @return
     */
    Long queryMonthAccWithdrawAmountByAccountNo(Long employeeId, String accountNo);

    Long queryTodayApplyTimes(Long employeeId);

    /**
     * 查询卡号本月的已提现金额（包含已到账和未到账）
     *
     * @param employeeId
     * @param accountNo
     * @return
     */
    Long queryMonthAccAmountCommited(Long employeeId, String accountNo);

}
