package com.anyi.common.exchange.service;


import com.anyi.common.exchange.domain.MbExchangeEmployeeInfo;
import com.anyi.common.exchange.mapper.MbExchangeEmployeeInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 二手机代理拓展信息表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
public class MbExchangeEmployeeInfoService extends ServiceImpl<MbExchangeEmployeeInfoMapper, MbExchangeEmployeeInfo> {

    public MbExchangeEmployeeInfo getByEmployeeId(Long employeeId){
        return this.lambdaQuery().eq(MbExchangeEmployeeInfo::getEmployeeId,employeeId).one();
    }

}
