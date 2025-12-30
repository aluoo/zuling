package com.anyi.common.mbr.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/2
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreOrderQuoteLogCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Integer finishNum = 0;

    private Integer dealNum = 0 ;
}