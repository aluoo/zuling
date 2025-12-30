package com.anyi.common.employee.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyCalVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成员数量")
    private Integer staffNum;

    @ApiModelProperty("门店数量")
    private Integer companyNum;

}
