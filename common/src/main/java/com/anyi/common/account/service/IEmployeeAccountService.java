package com.anyi.common.account.service;

import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.vo.EmployeeAccountVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.anyi.common.account.domain.EmployeeAccountLog;

/**
 * <p>
 * 个人账户表 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface IEmployeeAccountService extends IService<EmployeeAccount> {

    EmployeeAccount getByEmployeeId(Long employeeId);

    boolean changeAccountBalance(EmployeeAccountLog accountLog);

    EmployeeAccountVO getByEmployee(Long employeeId);
}
