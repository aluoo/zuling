package com.anyi.common.insurance.service;

import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.account.vo.EmployeeAccountVO;
import com.anyi.common.insurance.domain.DiCompanyAccount;
import com.anyi.common.insurance.domain.DiCompanyAccountLog;
import com.anyi.common.insurance.mapper.DiCompanyAccountMapper;
import com.anyi.common.insurance.response.InsuranceAccountVO;
import com.anyi.common.util.MoneyUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数保门店账户表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiCompanyAccountService extends ServiceImpl<DiCompanyAccountMapper, DiCompanyAccount>  {


    public DiCompanyAccount getByCompanyId(Long companyId) {
        return this.lambdaQuery()
                .eq(DiCompanyAccount::getCompanyId, companyId)
                .one();

    }


    public boolean changeAccountBalance(DiCompanyAccountLog accountLog) {
        int updates = this.getBaseMapper().changeAccountBalance(accountLog);
        return updates > 0;
    }


    public InsuranceAccountVO getByCompany(Long companyId) {
        InsuranceAccountVO resultVo = new InsuranceAccountVO();
        resultVo.setAmount(0L);
        DiCompanyAccount companyAccount = getByCompanyId(companyId);
        if (ObjectUtil.isNull(companyAccount)) return resultVo;

        resultVo.setAmount(companyAccount.getAbleBalance());
        resultVo.setId(companyAccount.getId());

        return resultVo;
    }


}
