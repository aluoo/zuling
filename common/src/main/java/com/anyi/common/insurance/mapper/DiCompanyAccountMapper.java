package com.anyi.common.insurance.mapper;

import com.anyi.common.account.domain.EmployeeAccountLog;
import com.anyi.common.insurance.domain.DiCompanyAccount;
import com.anyi.common.insurance.domain.DiCompanyAccountLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数保门店账户表 Mapper 接口
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
public interface DiCompanyAccountMapper extends BaseMapper<DiCompanyAccount> {

    int changeAccountBalance(@Param("employeeAccountLog") DiCompanyAccountLog employeeAccountLog);
}
