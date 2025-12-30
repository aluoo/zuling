package com.anyi.common.insurance.service;

import com.anyi.common.insurance.constant.CompanyAccountChangeEnum;
import com.anyi.common.insurance.domain.DiCompanyAccount;
import com.anyi.common.insurance.domain.DiCompanyAccountLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class CompanyAccountChangeService {


    @Autowired
    private DiCompanyAccountService companyAccountService;

    @Autowired
    private DiCompanyAccountLogService companyAccountLogService;

    /**
     * 账户金币变更 统一入口方法
     *
     * @param companyId        门店id
     * @param accountChangeEnum 变更类型枚举类
     * @param changeBalance     变更账户值
     * @param correlationId     关联对象ID
     * @return
     */
    public boolean changeAccount(Long companyId, CompanyAccountChangeEnum accountChangeEnum, long changeBalance, Long correlationId, String remark) {


        DiCompanyAccount employeeAccount = companyAccountService.lambdaQuery().eq(DiCompanyAccount::getCompanyId, companyId).last("for update").one();

        if (employeeAccount == null) {
            return false;
        }

        long changeAbleBalance = changeBalance * accountChangeEnum.getChangeAbleBalance(),
                changeTempFrozenBalance = changeBalance * accountChangeEnum.getChangeTempFrozenBalance(),
                changeFrozenBalance = changeBalance * accountChangeEnum.getChangeFrozenBalance(),
                changeAccumulateIncome = changeBalance * accountChangeEnum.getChangeAccumulateIncome(),
                changeAccAwardIncome = changeBalance * accountChangeEnum.getChangeAccAwardIncome(),
                changeAccWithdraw = changeBalance * accountChangeEnum.getChangeAccWithdraw();


        DiCompanyAccountLog accountLog = new DiCompanyAccountLog();
        accountLog.setCompanyId(employeeAccount.getCompanyId());
        accountLog.setCompanyAccountId(employeeAccount.getId());
        accountLog.setAncestors(employeeAccount.getAncestors());
        accountLog.setChangeMainType(accountChangeEnum.getChangeMainType());
        accountLog.setChangeDetailType(accountChangeEnum.getChangeDetailType());
        accountLog.setUserFocus(accountChangeEnum.getIsUserFocusType());
        accountLog.setChangeBalance(changeBalance);


        accountLog.setAbleBalanceBefore(employeeAccount.getAbleBalance());
        accountLog.setAbleBalanceChange(changeAbleBalance);
        accountLog.setAbleBalanceAfter(employeeAccount.getAbleBalance() + changeAbleBalance);

        accountLog.setTempFrozenBalanceBefore(employeeAccount.getTempFrozenBalance());
        accountLog.setTempFrozenBalanceChange(changeTempFrozenBalance);
        accountLog.setTempFrozenBalanceAfter(employeeAccount.getTempFrozenBalance() + changeTempFrozenBalance);

        accountLog.setFrozenBalanceBefore(employeeAccount.getFrozenBalance());
        accountLog.setFrozenBalanceChange(changeFrozenBalance);
        accountLog.setFrozenBalanceAfter(employeeAccount.getFrozenBalance() + changeFrozenBalance);

        accountLog.setAccumulateIncomeBefore(employeeAccount.getAccumulateIncome());
        accountLog.setAccumulateIncomeChange(changeAccumulateIncome);
        accountLog.setAccumulateIncomeAfter(employeeAccount.getAccumulateIncome() + changeAccumulateIncome);

        accountLog.setAccAwardIncomeBefore(employeeAccount.getAccAwardIncome());
        accountLog.setAccAwardIncomeChange(changeAccAwardIncome);
        accountLog.setAccAwardIncomeAfter(employeeAccount.getAccAwardIncome() + changeAccAwardIncome);

        accountLog.setAccWithdrawBefore(employeeAccount.getAccWithdraw());
        accountLog.setAccWithdrawChange(changeAccWithdraw);
        accountLog.setAccWithdrawAfter(employeeAccount.getAccWithdraw() + changeAccWithdraw);


        if (correlationId != null) {
            accountLog.setCorrelationId(correlationId);
        }

        accountLog.setRemark(remark);
        accountLog.setCreateTime(LocalDateTime.now());
        if (companyAccountLogService.save(accountLog)) {
            boolean changeSuccess = companyAccountService.changeAccountBalance(accountLog);
            return changeSuccess;
        }
        return false;
    }


}
