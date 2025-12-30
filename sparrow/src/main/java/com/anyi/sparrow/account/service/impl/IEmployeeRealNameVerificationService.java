package com.anyi.sparrow.account.service.impl;


import com.anyi.sparrow.account.domain.EmployeeRealNameVerification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
public interface IEmployeeRealNameVerificationService extends IService<EmployeeRealNameVerification> {

    EmployeeRealNameVerification getByEmployeeId(Long employeeId);

    Boolean isVerified(Long employeeId);
}