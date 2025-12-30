package com.anyi.sparrow.withdraw.dao.mapper;


import com.anyi.sparrow.withdraw.domain.WithdrawEmployeeApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 提现申请表 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface WithdrawEmployeeApplyMapper extends BaseMapper<WithdrawEmployeeApply> {

    Long queryAccWithdrawAmountByAccountNo(@Param("employeeId") Long employeeId,
                                           @Param("accountNo") String accountNo,
                                           @Param("startDate") Date startDate,
                                           @Param("stopDate") Date stopDate, @Param("status") int status);

    /**
     * 查询卡号已提现金额，包含在途金额
     *
     * @param employeeId
     * @param accountNo
     * @param startDate
     * @param stopDate
     * @return
     */
    Long queryAccWithdrawAmountCommitted(@Param("employeeId") Long employeeId, @Param("accountNo") String accountNo, @Param("startDate") Date startDate, @Param("stopDate") Date stopDate);
}
