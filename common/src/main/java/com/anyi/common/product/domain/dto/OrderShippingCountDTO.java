package com.anyi.common.product.domain.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShippingCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long recyclerCompanyId;
    private Integer count;

    private Long storeCompanyId;
    private Integer status;
    private List<Long> recyclerCompanyIds;
    private Date now;
    private Boolean verified;
    /**
     * 1统计今日待发货 2统计超时未发货
     */
    private Integer countType;

    @AllArgsConstructor
    public enum CountType {
        /**
         * 今日待发货
         */
        PENDING_SHIPMENT(1),
        /**
         * 超时未发货
         */
        OVERDUE(2),
        /**
         * 途中
         */
        PENDING_RECEIPT(null),
        ;

        @Getter
        private final Integer value;
    }
}