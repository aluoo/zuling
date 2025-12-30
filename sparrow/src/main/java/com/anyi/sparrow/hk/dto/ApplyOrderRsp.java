package com.anyi.sparrow.hk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2025/7/28
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyOrderRsp implements Serializable {
    private static final long serialVersionUID = 1L;

    private String order_sn;
    private String third_order_sn;

}