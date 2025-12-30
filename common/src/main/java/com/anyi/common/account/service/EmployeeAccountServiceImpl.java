package com.anyi.common.account.service;

import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.mapper.EmployeeAccountMapper;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.vo.EmployeeAccountVO;
import com.anyi.common.util.MoneyUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 个人账户表 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Service
public class EmployeeAccountServiceImpl extends ServiceImpl<EmployeeAccountMapper, EmployeeAccount> implements IEmployeeAccountService {


    @Override
    public EmployeeAccount getByEmployeeId(Long employeeId) {
        return this.lambdaQuery()
                .eq(EmployeeAccount::getEmployeeId, employeeId)
                .one();

    }

    @Override
    public boolean changeAccountBalance(EmployeeAccountLog accountLog) {
        int updates = this.getBaseMapper().changeAccountBalance(accountLog);
        return updates > 0;
    }

    @Override
    public EmployeeAccountVO getByEmployee(Long employeeId) {
        EmployeeAccountVO resultVo = new EmployeeAccountVO();
        EmployeeAccount employeeAccount = getByEmployeeId(employeeId);
        if (ObjectUtil.isNull(employeeAccount)) return resultVo;

        resultVo.setAbleBalanceStr(MoneyUtil.convert(employeeAccount.getAbleBalance()));
        resultVo.setTempFrozenBalanceStr(MoneyUtil.convert(employeeAccount.getTempFrozenBalance()));
        resultVo.setFrozenBalanceStr(MoneyUtil.convert(employeeAccount.getFrozenBalance()));
        resultVo.setAccumulateIncomeStr(MoneyUtil.convert(employeeAccount.getAccumulateIncome()));
        resultVo.setAccAwardIncomeStr(MoneyUtil.convert(employeeAccount.getAccAwardIncome()));
        resultVo.setAccWithdrawStr(MoneyUtil.convert(employeeAccount.getAccWithdraw()));
        return resultVo;
    }
}
