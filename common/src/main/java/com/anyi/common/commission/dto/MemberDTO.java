package com.anyi.common.commission.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberDTO {

    private int memberNum;
    private int deptNum;
    private List<EmployeeDTO> members;
}
