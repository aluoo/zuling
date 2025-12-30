package com.anyi.common.account.mapper;


import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.domain.EmployeeAccountLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 个人账户表 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface EmployeeAccountMapper extends BaseMapper<EmployeeAccount> {

    int changeAccountBalance(@Param("employeeAccountLog") EmployeeAccountLog employeeAccountLog);
}
