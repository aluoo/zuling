package com.anyi.common.commission.mapper;


import com.anyi.common.commission.domain.CommissionSettleCheck;
import com.anyi.common.commission.req.CommissionSettleCheckReq;
import com.anyi.common.commission.response.CommissionSettleCheckSumVO;
import com.anyi.common.commission.response.CommissionSettleCheckVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统结算单 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
public interface CommissionSettleCheckMapper extends BaseMapper<CommissionSettleCheck> {

    List<CommissionSettleCheckVO> selectByParam(CommissionSettleCheckReq req);

    CommissionSettleCheckSumVO SumByParam(CommissionSettleCheckReq req);
}