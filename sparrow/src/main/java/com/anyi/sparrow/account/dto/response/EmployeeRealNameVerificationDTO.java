package com.anyi.sparrow.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRealNameVerificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date createTime;
    private Date updateTime;
    private Boolean deleted;
    private Long employeeId;
    private String name;
    private String idCard;
    private String idUrlUp;
    private String idUrlDown;
    private String remoteResp;
}