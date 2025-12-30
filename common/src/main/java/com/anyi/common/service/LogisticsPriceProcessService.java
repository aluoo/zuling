package com.anyi.common.service;

import com.anyi.common.product.domain.dto.LogisticsPriceDTO;
import com.anyi.common.util.CurrencyUtils;
import com.lop.open.api.sdk.domain.ECAP.CommonQueryOrderApi.commonGetActualFeeInfoV1.CommonActualFeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/26
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class LogisticsPriceProcessService {
    @Autowired
    private CommonSysDictService dictService;

    public LogisticsPriceDTO calcLogisticsPrice(CommonActualFeeResponse req) {
        // 物流实际费用
        BigDecimal money = (req.getSumMoney() != null && CurrencyUtils.greaterThan(req.getSumMoney(), BigDecimal.ZERO)) ? req.getSumMoney() : req.getEstimateSumMoney();
        // 获取物流月结折扣费率
        BigDecimal discountRate = getDiscountRate();
        // 折扣金额
        BigDecimal discountPriceDecimal = BigDecimal.ZERO;
        // 原始物流费用
        BigDecimal originalPriceDecimal = money;
        if (CurrencyUtils.greaterThan(discountRate, BigDecimal.ZERO)) {
            // calc
            originalPriceDecimal = CurrencyUtils.divide(money, discountRate);
            discountPriceDecimal = CurrencyUtils.minus(originalPriceDecimal, money);
        }
        int price = CurrencyUtils.multiply(money, CurrencyUtils.HUNDRED).intValue();
        int discountPrice = CurrencyUtils.multiply(discountPriceDecimal, CurrencyUtils.HUNDRED).intValue();
        int originalPrice = CurrencyUtils.multiply(originalPriceDecimal, CurrencyUtils.HUNDRED).intValue();
        return LogisticsPriceDTO.builder()
                .money(money)
                .price(price)
                .originalPriceDecimal(originalPriceDecimal)
                .originalPrice(originalPrice)
                .discountRate(discountRate)
                .discountPriceDecimal(discountPriceDecimal)
                .discountPrice(discountPrice)
                .build();
    }

    private BigDecimal getDiscountRate() {
        BigDecimal discountRate = dictService.getJdlLogisticsDiscountRate();
        if (CurrencyUtils.greaterOrEqualsThan(discountRate, BigDecimal.ONE)) {
            discountRate = BigDecimal.ZERO;
        }
        if (CurrencyUtils.lowerOrEqualsThan(discountRate, BigDecimal.ZERO)) {
            discountRate = BigDecimal.ZERO;
        }
        return discountRate;
    }

}