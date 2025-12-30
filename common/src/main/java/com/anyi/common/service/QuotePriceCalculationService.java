package com.anyi.common.service;

import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.advice.BaseException;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.company.dto.PackageInfoDTO;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.company.service.PackageInfoService;
import com.anyi.common.product.domain.dto.QuoteCommissionInfoDTO;
import com.anyi.common.util.CurrencyUtils;
import com.anyi.common.util.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/12
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class QuotePriceCalculationService {
    @Autowired
    private PackageInfoService packageInfoService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private IEmployeeAccountService employeeAccountService;

    public void validateBalance(Long companyId, Integer actualPaymentPrice) {
        Long managerId = Optional.ofNullable(
                companyService.getCompanyManagerId(companyId)
        ).orElseThrow(() -> new BaseException(-1, "账户不存在"));
        EmployeeAccount managerAccount = Optional.ofNullable(
                employeeAccountService.getByEmployeeId(managerId)
        ).orElseThrow(() -> new BaseException(-1, "账户不存在"));

        int balance = managerAccount.getAbleBalance().intValue();
        if ((balance - actualPaymentPrice) < MoneyUtil.HUNDRED) {
            throw new BaseException(-1, "余额不足");
        }
    }

    public QuoteCommissionInfoDTO calcCommissionByType(Integer price, CommissionBizType bizType, Long companyId) {
        PackageInfoDTO interval = this.getPackageInfoByPriceInterval(price, bizType, companyId);
        if (interval == null) {
            return null;
        }
        Integer actualCommission = this.calc(price, interval.getRealCommissionScale(), interval.getRealCommissionFee());

        return QuoteCommissionInfoDTO.builder()
                .price(price)
                .actualCommissionRule(interval.getRealCommissionScale())
                .actualCommission(actualCommission)
                .build();
    }

    public QuoteCommissionInfoDTO calcPlatformSubsidyByType(Integer price, CommissionBizType bizType, Long companyId) {
        PackageInfoDTO interval = this.getPackageInfoByPriceInterval(price, bizType, companyId);
        if (interval == null) {
            return null;
        }

        return QuoteCommissionInfoDTO.builder()
                .price(price)
                .platformSubsidyPrice(interval.getPlatformSubsidyPrice().intValue())
                .build();
    }

    private PackageInfoDTO getPackageInfoByPriceInterval(Integer price, CommissionBizType bizType, Long companyId) {
        if (price == null) {
            price = 0;
        }
        PackageInfoDTO req = PackageInfoDTO.builder()
                .companyId(companyId)
                .bizTypeId(bizType.getType())
                .price(price)
                .build();
        PackageInfoDTO interval = packageInfoService.getPackageInfoByPriceInterval(req);
        if (interval != null) {
            return interval;
        }
        PackageInfoDTO lowest = packageInfoService.getLowestPackageInfoByPriceInterval(req);
        if (lowest != null && price <= lowest.getPriceLow()) {
            return lowest;
        }
        PackageInfoDTO highest = packageInfoService.getHighestPackageInfoByPriceInterval(req);
        if (highest != null && price >= highest.getPriceHigh()) {
            return highest;
        }
        return null;
    }

    private Integer calc(Integer price, BigDecimal scale, Long maxFee) {
        BigDecimal priceD = CurrencyUtils.toBigDecimal(MoneyUtil.fenToYuan(price));
        BigDecimal calcCommission = CurrencyUtils.multiply(priceD, scale);
        BigDecimal maxFeeD = CurrencyUtils.toBigDecimal(MoneyUtil.convert(maxFee));
        BigDecimal result = CurrencyUtils.greaterThan(calcCommission, maxFeeD) ? maxFeeD : calcCommission;
        return CurrencyUtils.multiply(result, CurrencyUtils.HUNDRED).intValue();
    }
}