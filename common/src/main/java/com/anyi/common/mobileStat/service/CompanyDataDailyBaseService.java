package com.anyi.common.mobileStat.service;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.commission.mapper.CommissionSettleDataDailyTotalMapper;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.employee.vo.AgencyVO;
import com.anyi.common.exchange.mapper.MbExchangeOrderMapper;
import com.anyi.common.insurance.mapper.DiInsuranceOrderMapper;
import com.anyi.common.mobileStat.domain.CompanyDataDailyBase;
import com.anyi.common.mobileStat.dto.CompanyStatDTO;
import com.anyi.common.mobileStat.mapper.CompanyDataDailyBaseMapper;
import com.anyi.common.mobileStat.response.CompanyStatVO;
import com.anyi.common.product.mapper.OrderMapper;
import com.anyi.common.product.mapper.OrderQuotePriceLogMapper;
import com.anyi.common.util.MoneyUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 门店统计日看板表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-08
 */
@Service
@Slf4j
public class CompanyDataDailyBaseService extends ServiceImpl<CompanyDataDailyBaseMapper, CompanyDataDailyBase> {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    private OrderMapper mbOrderMapper;
    @Autowired
    private OrderQuotePriceLogMapper orderQuotePriceLogMapper;
    @Autowired
    CommissionSettleDataDailyTotalMapper commissionSettleDataDailyTotalMapper;
    @Autowired
    private MbExchangeOrderMapper exchangeOrderMapper;
    @Autowired
    private DiInsuranceOrderMapper insuranceOrderMapper;

    public CompanyStatVO companyStat(CompanyStatDTO req) {
        //看某个门店的时候重置
        if (ObjectUtil.isNotNull(req.getEmployeeId())) {
            req.setAncestors(employeeService.getById(req.getEmployeeId()).getAncestors());
        }
        CompanyStatVO resultVo = new CompanyStatVO();
        resultVo.setTransNum(0);
        resultVo.setPriceNum(0);
        resultVo.setOrderNum(0);
        resultVo.setFinalPriceAmount(0L);
        resultVo.setCommissionAmount(0L);
        resultVo.setCancelNum(0);
        resultVo.setOvertimeNum(0);
        resultVo.setExchangeAllNum(0);
        resultVo.setHuanjiNum(0);
        resultVo.setHuanjiPassNum(0);
        resultVo.setAppleNum(0);
        resultVo.setApplePassNum(0);
        resultVo.setLvzhouNum(0);
        resultVo.setLvzhouPassNum(0);
        resultVo.setInsuranceALLNum(0);
        resultVo.setInsuranceAnyNum(0);
        resultVo.setInsuranceAzNum(0);
        resultVo.setInsuranceYbNum(0);
        resultVo.setInsuranceSpNum(0);
        resultVo.setInsuranceCareNum(0);
        resultVo.setCommissionAmountStr("0");
        resultVo.setFinalPriceAmountStr("0");

        //当前登陆人下级的
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .likeRight(Employee::getAncestors, req.getAncestors())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));

        List<Long> employeeIds = employeeList.stream().map(Employee::getId).collect(Collectors.toList());
        req.setEmployeeIds(employeeIds);
        CompanyStatVO statVO = this.baseMapper.companyStat(req);
        if (ObjectUtil.isNull(statVO)) return resultVo;

        statVO.setCommissionAmountStr(MoneyUtil.convert(statVO.getCommissionAmount()));
        statVO.setFinalPriceAmountStr(MoneyUtil.convert(statVO.getFinalPriceAmount()));
        return statVO;
    }


    public CompanyStatVO companyStatToday(CompanyStatDTO req) {
        //看某个门店的时候重置
        if (ObjectUtil.isNotNull(req.getEmployeeId())) {
            req.setAncestors(employeeService.getById(req.getEmployeeId()).getAncestors());
        }
        CompanyStatVO resultVo = new CompanyStatVO();
        resultVo.setTransNum(0);
        resultVo.setPriceNum(0);
        resultVo.setOrderNum(0);
        resultVo.setFinalPriceAmount(0L);
        resultVo.setCommissionAmount(0L);
        resultVo.setCancelNum(0);
        resultVo.setOvertimeNum(0);
        resultVo.setExchangeAllNum(0);
        resultVo.setHuanjiNum(0);
        resultVo.setHuanjiPassNum(0);
        resultVo.setAppleNum(0);
        resultVo.setApplePassNum(0);
        resultVo.setLvzhouNum(0);
        resultVo.setLvzhouPassNum(0);
        resultVo.setInsuranceALLNum(0);
        resultVo.setInsuranceAnyNum(0);
        resultVo.setInsuranceAzNum(0);
        resultVo.setInsuranceYbNum(0);
        resultVo.setInsuranceSpNum(0);
        resultVo.setInsuranceCareNum(0);
        resultVo.setCommissionAmountStr("0");
        resultVo.setFinalPriceAmountStr("0");

        //当前登陆人下级的
        List<Employee> employeeList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .likeRight(Employee::getAncestors, req.getAncestors())
                 .eq(req.getCompanyId()!=null,Employee::getCompanyId,req.getCompanyId()));

        if(CollUtil.isEmpty(employeeList)) return resultVo;

        List<Long> employeeIds = employeeList.stream().map(Employee::getId).collect(Collectors.toList());

        //每日回收数量
        CompanyDataDailyBase transNum = mbOrderMapper.companyTransStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setTransNum(Optional.ofNullable(transNum).map(CompanyDataDailyBase::getTransNum).orElse(0));

        //询价数量
        CompanyDataDailyBase orderNum = mbOrderMapper.companyOrderStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setOrderNum(Optional.ofNullable(orderNum).map(CompanyDataDailyBase::getOrderNum).orElse(0));

        //报价数量 成交金额
        CompanyDataDailyBase priceNum = mbOrderMapper.priceOrderStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setPriceNum(Optional.ofNullable(priceNum).map(CompanyDataDailyBase::getPriceNum).orElse(0));
        resultVo.setFinalPriceAmount(Optional.ofNullable(priceNum).map(CompanyDataDailyBase::getFinalPriceAmount).orElse(0L));

        //取消次数
        CompanyDataDailyBase cancelNum = mbOrderMapper.cancelStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setCancelNum(Optional.ofNullable(cancelNum).map(CompanyDataDailyBase::getCancelNum).orElse(0));

        //作废次数
        CompanyDataDailyBase overTimeNum = mbOrderMapper.overTimeStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setOvertimeNum(Optional.ofNullable(overTimeNum).map(CompanyDataDailyBase::getOvertimeNum).orElse(0));

        //拉新模块统计
        CompanyStatVO  exchangeStatVo = exchangeOrderMapper.statPassGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setExchangeAllNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getExchangeAllNum).orElse(0));
        resultVo.setHuanjiNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getHuanjiNum).orElse(0));
        resultVo.setHuanjiPassNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getHuanjiPassNum).orElse(0));
        resultVo.setLvzhouNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getLvzhouNum).orElse(0));
        resultVo.setLvzhouPassNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getLvzhouPassNum).orElse(0));
        resultVo.setAppleNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getAppleNum).orElse(0));
        resultVo.setApplePassNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getApplePassNum).orElse(0));

        //数保模块统计
        CompanyStatVO insuranceStat = insuranceOrderMapper.statGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setInsuranceAnyNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceAnyNum).orElse(0));
        resultVo.setInsuranceSpNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceSpNum).orElse(0));
        resultVo.setInsuranceYbNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceYbNum).orElse(0));
        resultVo.setInsuranceCareNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceCareNum).orElse(0));
        resultVo.setInsuranceAzNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceAzNum).orElse(0));

        resultVo.setCommissionAmountStr(MoneyUtil.convert(resultVo.getCommissionAmount()));
        resultVo.setFinalPriceAmountStr(MoneyUtil.convert(resultVo.getFinalPriceAmount()));
        return resultVo;
    }

    public CompanyStatVO personStat(CompanyStatDTO req) {

        CompanyStatVO resultVo = new CompanyStatVO();
        resultVo.setTransNum(0);
        resultVo.setPriceNum(0);
        resultVo.setOrderNum(0);
        resultVo.setFinalPriceAmount(0L);
        resultVo.setCommissionAmount(0L);
        resultVo.setCancelNum(0);
        resultVo.setOvertimeNum(0);
        resultVo.setExchangeAllNum(0);
        resultVo.setHuanjiNum(0);
        resultVo.setHuanjiPassNum(0);
        resultVo.setAppleNum(0);
        resultVo.setApplePassNum(0);
        resultVo.setLvzhouNum(0);
        resultVo.setLvzhouPassNum(0);
        resultVo.setInsuranceALLNum(0);
        resultVo.setInsuranceAnyNum(0);
        resultVo.setInsuranceAzNum(0);
        resultVo.setInsuranceYbNum(0);
        resultVo.setInsuranceSpNum(0);
        resultVo.setInsuranceCareNum(0);
        resultVo.setCommissionAmountStr("0");
        resultVo.setFinalPriceAmountStr("0");


        List<Long> employeeIds = Arrays.asList(req.getEmployeeId());

        //每日回收数量
        CompanyDataDailyBase transNum = mbOrderMapper.companyTransStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setTransNum(Optional.ofNullable(transNum).map(CompanyDataDailyBase::getTransNum).orElse(0));

        //询价数量
        CompanyDataDailyBase orderNum = mbOrderMapper.companyOrderStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setOrderNum(Optional.ofNullable(orderNum).map(CompanyDataDailyBase::getOrderNum).orElse(0));

        //报价数量 成交金额
        CompanyDataDailyBase priceNum = mbOrderMapper.priceOrderStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setPriceNum(Optional.ofNullable(priceNum).map(CompanyDataDailyBase::getPriceNum).orElse(0));
        resultVo.setFinalPriceAmount(Optional.ofNullable(priceNum).map(CompanyDataDailyBase::getFinalPriceAmount).orElse(0L));

        //取消次数
        CompanyDataDailyBase cancelNum = mbOrderMapper.cancelStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setCancelNum(Optional.ofNullable(cancelNum).map(CompanyDataDailyBase::getCancelNum).orElse(0));

        //作废次数
        CompanyDataDailyBase overTimeNum = mbOrderMapper.overTimeStatGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setOvertimeNum(Optional.ofNullable(overTimeNum).map(CompanyDataDailyBase::getOvertimeNum).orElse(0));

        //拉新模块统计
        CompanyStatVO  exchangeStatVo = exchangeOrderMapper.statPassGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setExchangeAllNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getExchangeAllNum).orElse(0));
        resultVo.setHuanjiNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getHuanjiNum).orElse(0));
        resultVo.setHuanjiPassNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getHuanjiPassNum).orElse(0));
        resultVo.setLvzhouNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getLvzhouNum).orElse(0));
        resultVo.setLvzhouPassNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getLvzhouPassNum).orElse(0));
        resultVo.setAppleNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getAppleNum).orElse(0));
        resultVo.setApplePassNum(Optional.ofNullable(exchangeStatVo).map(CompanyStatVO::getApplePassNum).orElse(0));

        //数保模块统计
        CompanyStatVO insuranceStat = insuranceOrderMapper.statGroupByEmployee(req.getStartTime(), req.getEndTime(),employeeIds);
        resultVo.setInsuranceAnyNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceAnyNum).orElse(0));
        resultVo.setInsuranceSpNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceSpNum).orElse(0));
        resultVo.setInsuranceYbNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceYbNum).orElse(0));
        resultVo.setInsuranceCareNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceCareNum).orElse(0));
        resultVo.setInsuranceAzNum(Optional.ofNullable(insuranceStat).map(CompanyStatVO::getInsuranceAzNum).orElse(0));

        resultVo.setCommissionAmountStr(MoneyUtil.convert(resultVo.getCommissionAmount()));
        resultVo.setFinalPriceAmountStr(MoneyUtil.convert(resultVo.getFinalPriceAmount()));
        return resultVo;
    }


}
