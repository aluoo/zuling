package com.anyi.common.commission.service;


import com.anyi.common.commission.domain.CommissionSettleDataDailyTotal;
import com.anyi.common.commission.mapper.CommissionSettleDataDailyTotalMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Slf4j
@Service
public class CommissionSettleDataDailyTotalService extends ServiceImpl<CommissionSettleDataDailyTotalMapper, CommissionSettleDataDailyTotal> implements IService<CommissionSettleDataDailyTotal> {
}