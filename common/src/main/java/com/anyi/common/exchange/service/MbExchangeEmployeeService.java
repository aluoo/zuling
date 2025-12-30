package com.anyi.common.exchange.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.exchange.domain.MbExchangeEmployee;
import com.anyi.common.exchange.mapper.MbExchangeEmployeeMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 合伙人换机包 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Service
public class MbExchangeEmployeeService extends ServiceImpl<MbExchangeEmployeeMapper, MbExchangeEmployee>{

    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    CompanyService companyService;

    public List<Long> employeePhoneId(Long employeeId){
        Employee employee = employeeMapper.selectById(employeeId);
        if(ObjectUtil.isNull(employee)){
            throw new BusinessException(99999,"员工不存在");
        }
        /*Employee bd = employeeMapper.selectBdByAncestors(employee.getAncestors());
        if(ObjectUtil.isNull(bd)){
            throw new BusinessException(99999,"BD不存在");
        }*/
        Company company = companyService.getById(employee.getCompanyId());

        //门店对应的换机包
        List<MbExchangeEmployee> shopPhoneList = this.list(Wrappers.lambdaQuery(MbExchangeEmployee.class)
                .eq(MbExchangeEmployee::getEmployeeId,company.getEmployeeId()));

        if(CollUtil.isEmpty(shopPhoneList)){
           return null;
        }

        return shopPhoneList.stream().map(MbExchangeEmployee::getExchangePhoneId).collect(Collectors.toList());


    }

}
