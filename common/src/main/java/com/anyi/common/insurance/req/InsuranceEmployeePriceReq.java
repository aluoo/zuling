package com.anyi.common.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/23
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceEmployeePriceReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("SKUID")
    @NotNull(message = "SKUID")
    public Long skuId;


    @ApiModelProperty("员工ID后端自己传")
    private Long employeeId;

}