package com.anyi.common.commission.service;


import com.anyi.common.commission.domain.CommissionSettleDataDailyDetail;
import com.anyi.common.commission.mapper.CommissionSettleDataDailyDetailMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/13
 */
@Slf4j
@Service
public class CommissionSettleDataDailyDetailService extends ServiceImpl<CommissionSettleDataDailyDetailMapper, CommissionSettleDataDailyDetail> implements IService<CommissionSettleDataDailyDetail> {
}