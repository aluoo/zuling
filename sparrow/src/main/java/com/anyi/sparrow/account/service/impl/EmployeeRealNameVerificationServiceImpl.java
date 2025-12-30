package com.anyi.sparrow.account.service.impl;


import com.anyi.sparrow.account.dao.mapper.EmployeeRealNameVerificationMapper;
import com.anyi.sparrow.account.domain.EmployeeRealNameVerification;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Slf4j
@Service
public class EmployeeRealNameVerificationServiceImpl extends ServiceImpl<EmployeeRealNameVerificationMapper, EmployeeRealNameVerification> implements IEmployeeRealNameVerificationService {

    @Override
    public EmployeeRealNameVerification getByEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return null;
        }
        return this.lambdaQuery()
                .eq(EmployeeRealNameVerification::getDeleted, false)
                .eq(EmployeeRealNameVerification::getEmployeeId, employeeId)
                .one();
    }

    @Override
    public Boolean isVerified(Long employeeId) {
        EmployeeRealNameVerification bean = this.lambdaQuery()
                .eq(EmployeeRealNameVerification::getDeleted, false)
                .eq(EmployeeRealNameVerification::getEmployeeId, employeeId)
                .one();
        return bean != null;
    }
}