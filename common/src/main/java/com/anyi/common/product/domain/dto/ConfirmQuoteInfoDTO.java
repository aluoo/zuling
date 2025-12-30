package com.anyi.common.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/18
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmQuoteInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long confirmedQuoteId;
    private List<Long> notConfirmedQuoteIds;
}