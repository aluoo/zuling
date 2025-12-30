package com.anyi.common.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/26
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsPriceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long shippingOrderId;
    private Long recyclerCompanyId;

    /**
     * 实际物流费用
     */
    private BigDecimal money;
    private Integer price;
    /**
     * 物流月结折扣费率
     */
    private BigDecimal discountRate;
    /**
     * 折扣金额 = 原始物流费用-实际物流费用
     */
    private BigDecimal discountPriceDecimal;
    private Integer discountPrice;
    /**
     * 折扣前原始物流费用 = 实际物流费用 / 折扣费率
     */
    private BigDecimal originalPriceDecimal;
    private Integer originalPrice;
}