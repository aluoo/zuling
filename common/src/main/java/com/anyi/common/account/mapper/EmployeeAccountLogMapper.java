package com.anyi.common.account.mapper;


import com.anyi.common.account.domain.EmployeeAccountLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 个人账户变动明细表 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface EmployeeAccountLogMapper extends BaseMapper<EmployeeAccountLog> {

//    List<EmployeeAccountLog> queryUserAccountLog(@Param("employeeId") Long employeeId, @Param("userFocusTypeEnum") UserFocusTypeEnum userFocusTypeEnum, @Param("startDate") Date startDate, @Param("stopDate") Date stopDate);
}
