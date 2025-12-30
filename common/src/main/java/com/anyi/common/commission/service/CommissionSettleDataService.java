package com.anyi.common.commission.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.advice.SystemError;
import com.anyi.common.commission.domain.CommissionSettleDataDailyBase;
import com.anyi.common.commission.domain.CommissionSettleDataDailyDetail;
import com.anyi.common.commission.domain.CommissionSettleDataDailyTotal;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionSettleGainType;
import com.anyi.common.commission.response.IncomeDataDailyBizDetailVO;
import com.anyi.common.commission.response.IncomeDataDailyDetailVO;
import com.anyi.common.commission.response.IncomeDataDailyTotalVO;
import com.anyi.common.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Slf4j
@Service
public class CommissionSettleDataService {
    @Autowired
    private CommissionSettleDataDailyBaseService dailyBaseService;
    @Autowired
    private CommissionSettleDataDailyTotalService dailyTotalService;
    @Autowired
    private CommissionSettleDataDailyDetailService dailyDetailService;
    @Autowired
    private EmployeeService employeeService;

    public static final String PATTERN_YYYY_MM = "yyyy年MM月";
    public static final String PATTERN_MM_dd = "MM月dd日";
    public static final String ZERO = "0";
    public static final String HUNDRED = "100";

    private static DateTime parseDate(String monthInput) {
        DateTime dt;
        if (StrUtil.isBlank(monthInput)) {
            // 没有传参，默认取当月
            DateTime nowDt = DateUtil.date();
            DateTime beginOfMonth = DateUtil.beginOfMonth(nowDt);
            String format = DateUtil.format(beginOfMonth, PATTERN_YYYY_MM);
            dt = DateUtil.parse(format, PATTERN_YYYY_MM);
        } else {
            try {
                dt = DateUtil.parse(monthInput, PATTERN_YYYY_MM);
            } catch (Exception e) {
                throw new BusinessException(SystemError.INVALID_PARAMETER, "时间参数错误，格式为yyyy年MM月!");
            }
        }
        return dt;
    }

    public IncomeDataDailyTotalVO incomeDataDailyTotal(String monthInput, Long employeeId) {
        DateTime monthDt = parseDate(monthInput);
        String monthStr = DateUtil.format(monthDt, PATTERN_YYYY_MM);
        IncomeDataDailyTotalVO vo = IncomeDataDailyTotalVO.builder()
                .month(monthStr)
                .list(new ArrayList<>())
                .totalValue(ZERO)
                .build();

        return buildDailyTotalData(vo, monthDt, employeeId);
    }

    public IncomeDataDailyDetailVO incomeDataDailyDetail(Date day, Long employeeId) {
        DateTime dayDt = DateUtil.date(day);
        String dayStr = DateUtil.format(dayDt, DatePattern.CHINESE_DATE_PATTERN);
        IncomeDataDailyDetailVO vo = IncomeDataDailyDetailVO.builder()
                .day(dayStr)
                .totalValue(ZERO)
                .self(new ArrayList<>())
                .selfTotalValue(ZERO)
                .teams(new ArrayList<>())
                .teamsTotalValue(ZERO)
                .build();
        return buildDailyDetailData(vo, dayDt, employeeId);
    }

    public IncomeDataDailyBizDetailVO incomeDataDailyBizDetail(Long id, Long employeeId) {
        CommissionSettleDataDailyBase base = dailyBaseService.lambdaQuery()
                .eq(CommissionSettleDataDailyBase::getId, id)
                .eq(CommissionSettleDataDailyBase::getEmployeeId, employeeId)
                .one();
        if (base == null) {
            return null;
        }
        DateTime dayDt = DateUtil.date(base.getDay());
        String dayStr = DateUtil.format(dayDt, DatePattern.CHINESE_DATE_PATTERN);
        IncomeDataDailyBizDetailVO vo = IncomeDataDailyBizDetailVO.builder()
                .day(dayStr)
                .bizType(base.getBizType())
                .gainType(base.getGainType())
                .pageTitle(buildDesc(base.getGainType(), base.getBizType()))
                .totalValue(convertMoney(base.getValue()))
                .build();

        List<CommissionSettleDataDailyDetail> details = dailyDetailService.lambdaQuery().eq(CommissionSettleDataDailyDetail::getDailyBaseId, id).list();
        List<IncomeDataDailyBizDetailVO.Detail> list = new ArrayList<>();
        details.forEach(bean -> {
            String title = bean.getRemark();
            IncomeDataDailyBizDetailVO.Detail dto = IncomeDataDailyBizDetailVO.Detail.builder()
                    .value(convertMoney(bean.getValue()))
                    .title(title)
                    .build();
            list.add(dto);
        });
        vo.setList(list);
        return vo;
    }

    private String buildDesc(Integer gainType, Integer bizType) {
        String desc = "";
        if (bizType.equals(CommissionBizType.PLAT_SERVICE.getType())) {
            desc = "交易服务费佣金结算（{}）";
        }
        if (bizType.equals(CommissionBizType.APP_NEW.getType())) {
            desc = "App拉新佣金结算（{}）";
        }
        if (bizType.equals(CommissionBizType.PHONE_DOWN.getType())) {
            desc = "手机回收拉新佣金结算（{}）";
        }
        if (bizType.equals(CommissionBizType.INSURANCE_SERVICE.getType())) {
            desc = "数保佣金结算（{}）";
        }
        CommissionSettleGainType gainTypeEnum = EnumUtil.getBy(CommissionSettleGainType::getType, gainType);
        switch (gainTypeEnum) {
            case BY_MYSELF: {
                desc = StrUtil.format(desc, "个人");
                break;
            }
            case CHILD_CONTRIBUTE: {
                desc = StrUtil.format(desc, "团队贡献");
                break;
            }
            default: {
                break;
            }
        }
        return desc;
    }

    private IncomeDataDailyTotalVO buildDailyTotalData(IncomeDataDailyTotalVO vo, DateTime dateTime, Long employeeId) {
        List<IncomeDataDailyTotalVO.DailyData> list = new ArrayList<>();
        String content = "{}佣金结算";
        DateTime beginOfMonth = DateUtil.beginOfMonth(dateTime);
        DateTime endOfMonth = DateUtil.endOfMonth(dateTime);
        List<CommissionSettleDataDailyTotal> totals = dailyTotalService.lambdaQuery()
                .eq(CommissionSettleDataDailyTotal::getEmployeeId, employeeId)
                .between(CommissionSettleDataDailyTotal::getDay, beginOfMonth.toJdkDate(), endOfMonth.toJdkDate())
                .list();
        if (CollUtil.isEmpty(totals)) {
            return vo;
        }
        int totalValue = 0;

        for (CommissionSettleDataDailyTotal total : totals) {
            String dateFormat = DateUtil.format(total.getDay(), PATTERN_MM_dd);
            String title = StrUtil.format(content, dateFormat);
            Integer value = total.getValue();
            IncomeDataDailyTotalVO.DailyData data = IncomeDataDailyTotalVO.DailyData.builder()
                    .day(total.getDay())
                    .title(title)
                    .value(convertMoney(value))
                    .build();
            list.add(data);
            totalValue += value;
        }
        vo.setList(list);
        vo.setTotalValue(convertMoney(totalValue));
        return vo;
    }

    private IncomeDataDailyDetailVO buildDailyDetailData(IncomeDataDailyDetailVO vo, DateTime dayDt, Long employeeId) {
        List<CommissionSettleDataDailyBase> bases = dailyBaseService.lambdaQuery()
                .eq(CommissionSettleDataDailyBase::getEmployeeId, employeeId)
                .eq(CommissionSettleDataDailyBase::getDay, dayDt.toJdkDate())
                .list();
        if (CollUtil.isEmpty(bases)) {
            return vo;
        }
        int totalValue = bases.stream().mapToInt(CommissionSettleDataDailyBase::getValue).sum();
        vo.setTotalValue(convertMoney(totalValue));

        int selfTotalValue = 0;
        int teamsTotalValue = 0;
        for (CommissionSettleDataDailyBase base : bases) {
            IncomeDataDailyDetailVO.DetailData data = BeanUtil.copyProperties(base, IncomeDataDailyDetailVO.DetailData.class);
            data.setValue(convertMoney(base.getValue()));

            if (base.getBizType().intValue() == CommissionBizType.PLAT_SERVICE.getType().intValue()) {
                if (base.getGainType().equals(CommissionSettleGainType.BY_MYSELF.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("交易服务费佣金结算（个人）");
                    vo.getSelf().add(data);
                }
                if (base.getGainType().equals(CommissionSettleGainType.CHILD_CONTRIBUTE.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("交易服务费佣金结算（团队贡献）");
                    vo.getSelf().add(data);
                }

            }
            if (base.getBizType().intValue() == CommissionBizType.APP_NEW.getType().intValue()) {
                if (base.getGainType().equals(CommissionSettleGainType.BY_MYSELF.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("App拉新佣金结算（个人）");
                    vo.getSelf().add(data);
                }
                if (base.getGainType().equals(CommissionSettleGainType.CHILD_CONTRIBUTE.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("App拉新佣金结算（团队贡献）");
                    vo.getSelf().add(data);
                }
            }
            if (base.getBizType().intValue() == CommissionBizType.PHONE_DOWN.getType().intValue()) {
                if (base.getGainType().equals(CommissionSettleGainType.BY_MYSELF.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("手机回收佣金结算（个人）");
                    vo.getSelf().add(data);
                }
                if (base.getGainType().equals(CommissionSettleGainType.CHILD_CONTRIBUTE.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("手机回收佣金结算（团队贡献）");
                    vo.getSelf().add(data);
                }
            }
            if (base.getBizType().intValue() == CommissionBizType.INSURANCE_SERVICE.getType().intValue()) {
                if (base.getGainType().equals(CommissionSettleGainType.BY_MYSELF.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("数保佣金结算（个人）");
                    vo.getSelf().add(data);
                }
                if (base.getGainType().equals(CommissionSettleGainType.CHILD_CONTRIBUTE.getType())) {
                    selfTotalValue += base.getValue();
                    data.setTitle("数保佣金结算（团队贡献）");
                    vo.getSelf().add(data);
                }
            }
        }
        vo.setSelfTotalValue(convertMoney(selfTotalValue));
        vo.setTeamsTotalValue(convertMoney(teamsTotalValue));
        return vo;
    }

    private String convertMoney(int value) {
        return new BigDecimal(value).divide(new BigDecimal(HUNDRED), 2, RoundingMode.UNNECESSARY).toString();
    }
}