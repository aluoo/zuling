package com.anyi.sparrow.product.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.service.CommissionSettleService;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.product.domain.Order;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.anyi.common.product.domain.response.StoreCenterIndexVO;
import com.anyi.common.product.service.OrderService;
import com.anyi.common.util.MoneyUtil;
import com.anyi.sparrow.account.service.impl.IEmployeeRealNameVerificationService;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class StoreCenterService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private IEmployeeRealNameVerificationService employeeRealNameVerificationService;
    @Autowired
    private IEmployeeAccountService employeeAccountService;
    @Autowired
    private CommissionSettleService commissionSettleService;

    public StoreCenterIndexVO index() {
        StoreCenterIndexVO vo = StoreCenterIndexVO.builder().build();
        // 用户信息
        vo.setUserInfo(buildUserInfo());
        // 今日收入
        vo.setBillInfo(buildBillInfo());
        // 手机订单中心
        vo.setOrderInfo(buildOrderInfo());
        // 手机发货中心
        vo.setShippingOrderInfo(buildShippingOrderInfo());
        return vo;
    }

    private StoreCenterIndexVO.UserInfo buildUserInfo() {
        LoginUser user = LoginUserContext.getUser();
        Company company = companyService.lambdaQuery().eq(Company::getId, user.getCompanyId()).one();
        Boolean verified = employeeRealNameVerificationService.isVerified(user.getId());
        EmployeeAccount account = employeeAccountService.getByEmployeeId(user.getId());
        // long balance = account.getAbleBalance() + account.getTempFrozenBalance();
        long balance = account.getAbleBalance();
        return StoreCenterIndexVO.UserInfo.builder()
                .employeeId(user.getId())
                .employeeName(user.getName())
                .employeeMobile(user.getMobileNumber())
                .companyId(user.getCompanyId())
                .companyName(company.getName())
                //连锁店创建的普通部门管理员也算店长类型
                .manage(user.getDeptType()==1 || (user.getCompanyType()==3 && user.getType()==3))
                .employeeType(user.getType())
                .companyType(user.getCompanyType())
                .realNameVerified(verified)
                .balance(MoneyUtil.convert(balance))
                .level(user.getLevel())
                .build();
    }

    private StoreCenterIndexVO.TodayIncomeInfo buildBillInfo() {
        // 从commission_settle里面拉数据，时间是createTime
        String today = DateUtil.format(DateUtil.date(), DatePattern.CHINESE_DATE_PATTERN);
        DateTime beginTime = DateUtil.beginOfDay(DateUtil.date());
        DateTime endTime = DateUtil.endOfDay(DateUtil.date());
        Long employeeId = LoginUserContext.getUser().getId();
        Long mobileOrderIncome = commissionSettleService.getBaseMapper().incomeStatisticsByDate(employeeId, CommissionBizType.PHONE_DOWN.getType().intValue(), beginTime.toJdkDate(), endTime.toJdkDate());
        Long appNewOrderIncome = commissionSettleService.getBaseMapper().incomeStatisticsByDate(employeeId, CommissionBizType.APP_NEW.getType().intValue(), beginTime.toJdkDate(), endTime.toJdkDate());
        Long totalIncome = mobileOrderIncome + appNewOrderIncome;
        return StoreCenterIndexVO.TodayIncomeInfo.builder()
                .today(today)
                .totalIncome(MoneyUtil.convert(totalIncome))
                .appNewOrderIncome(MoneyUtil.convert(appNewOrderIncome))
                .mobileOrderIncome(MoneyUtil.convert(mobileOrderIncome))
                .build();
    }

    private StoreCenterIndexVO.OrderInfo buildOrderInfo() {
        Long unchecked = orderService.lambdaQuery()
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.UNCHECKED.getCode())
                .count();
        Long pendingPayment = orderService.lambdaQuery()
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_PAYMENT.getCode())
                .count();
        Long pendingShipment = orderService.lambdaQuery()
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .count();
        return StoreCenterIndexVO.OrderInfo.builder()
                .unchecked(unchecked.intValue())
                .pendingPayment(pendingPayment.intValue())
                .pendingShipment(pendingShipment.intValue())
                .afterSale(0)
                .build();
    }

    private StoreCenterIndexVO.ShippingOrderInfo buildShippingOrderInfo() {
        Long pendingShipment = orderService.lambdaQuery()
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_SHIPMENT.getCode())
                .count();
        Long pendingReceipt = orderService.lambdaQuery()
                .eq(Order::getStoreCompanyId, LoginUserContext.getUser().getCompanyId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .eq(Order::getStatus, OrderStatusEnum.PENDING_RECEIPT.getCode())
                .count();
        return StoreCenterIndexVO.ShippingOrderInfo.builder()
                .pendingShipment(pendingShipment.intValue())
                .pendingReceipt(pendingReceipt.intValue())
                .build();
    }
}