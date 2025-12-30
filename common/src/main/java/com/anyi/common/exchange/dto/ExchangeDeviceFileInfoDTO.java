package com.anyi.common.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/9/5
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeDeviceFileInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long employeeId;

    private String orderNo;

    private String url;

    private List<String> files;
}