package com.anyi.common.commission.mapper;


import com.anyi.common.commission.domain.CommissionSettle;
import com.anyi.common.commission.dto.income.TeamWaitSettlesDetailDTO;
import com.anyi.common.commission.dto.income.WaitSettlesDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统结算单 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface CommissionSettleMapper extends BaseMapper<CommissionSettle> {

    Long personSettleStats(@Param("employeeId") Long employeeId, @Param("bizType") int bizType, @Param("gainType") int gainType, @Param("settleStatus") int settleStatus);

    List<WaitSettlesDTO> queryPersonSettles(@Param("employeeId") Long employeeId, @Param("bizType") int bizType, @Param("gainType") int gainType, @Param("settleStatus") int settleStatus);

    List<TeamWaitSettlesDetailDTO> queryTeamSettles(@Param("employeeId") Long employeeId, @Param("bizType") int bizType, @Param("gainType") int gainType, @Param("settleStatus") int settleStatus);

    Long incomeStatisticsByDate(@Param("employeeId") Long employeeId,
                                @Param("bizType") Integer bizType,
                                @Param("beginTime") Date beginTime,
                                @Param("endTime") Date endTime);
}