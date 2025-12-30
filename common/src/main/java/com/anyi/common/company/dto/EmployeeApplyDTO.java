package com.anyi.common.company.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class EmployeeApplyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "邀请员工手机号不能为空")
    private Long employeeId;

    private String mobileNumber;

    private String name;


}