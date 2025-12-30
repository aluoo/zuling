package com.anyi.common.account.vo;

import com.anyi.common.domain.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 个人账户表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Data
public class CompanyEmployeeVO extends BaseEntity implements Serializable {

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("门店照片")
    private String frontUrl;

    @ApiModelProperty("门店照片")
    private String employeePhone;

    @ApiModelProperty("是否负责人")
    private Boolean manage;

    @ApiModelProperty("门店名称")
    private String companyName;


}
