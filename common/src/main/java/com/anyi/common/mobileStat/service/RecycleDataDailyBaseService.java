package com.anyi.common.mobileStat.service;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.mobileStat.domain.RecycleDataDailyBase;
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.mapper.RecycleDataDailyBaseMapper;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.anyi.common.mobileStat.response.RecycleStatVO;
import com.anyi.common.util.MoneyUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 回收商统计日看板表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Service
@Slf4j
public class RecycleDataDailyBaseService extends ServiceImpl<RecycleDataDailyBaseMapper, RecycleDataDailyBase> {

    @Autowired
    EmployeeService employeeService;

    public RecycleStatVO recycleStat(CompanyStatDTO req) {
        //看某个门店的时候重置
        if (ObjectUtil.isNotNull(req.getEmployeeId())) {
            req.setAncestors(employeeService.getById(req.getEmployeeId()).getAncestors());
        }
        RecycleStatVO resultVo = new RecycleStatVO();
        resultVo.setTransNum(0);
        resultVo.setTransAmount(0L);
        resultVo.setTransAmountStr("0.00");
        resultVo.setAvgTransAmountStr("0.00");
        resultVo.setRefundAmount(0L);
        resultVo.setQuotePriceNum(0);
        resultVo.setQuoteTimeSpent(0L);
        resultVo.setAvgQuoteTimeSpentStr("0.00");
        resultVo.setOrderConfirmNum(0);
        //当前登陆人下级的
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .likeRight(Employee::getAncestors, req.getAncestors())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));

        List<Long> employeeIds = employeeList.stream().map(Employee::getId).collect(Collectors.toList());
        req.setEmployeeIds(employeeIds);
        RecycleStatVO statVO = this.baseMapper.recycleStat(req);
        if (ObjectUtil.isNull(statVO)) return resultVo;

        statVO.setTransAmountStr(MoneyUtil.convert(statVO.getTransAmount()));
        if (statVO.getTransNum() > 0) {
            statVO.setAvgTransAmountStr(NumberUtil.decimalFormat("0.00", NumberUtil.div(new BigDecimal(statVO.getTransAmountStr()), statVO.getTransNum())));
        } else {
            statVO.setAvgTransAmountStr("0.00");
        }
        statVO.setRefundAmountStr(MoneyUtil.convert(statVO.getRefundAmount()));
        statVO.setQuoteTimeSpentStr(NumberUtil.decimalFormat("0.00", NumberUtil.div(BigDecimal.valueOf(statVO.getQuoteTimeSpent()), 60000)));
        if (statVO.getQuotePriceNum() > 0) {
            statVO.setAvgQuoteTimeSpentStr(NumberUtil.decimalFormat("0.00", NumberUtil.div(new BigDecimal(statVO.getQuoteTimeSpentStr()), statVO.getQuotePriceNum())));
        } else {
            resultVo.setAvgQuoteTimeSpentStr("0");
        }

        return statVO;
    }


}
