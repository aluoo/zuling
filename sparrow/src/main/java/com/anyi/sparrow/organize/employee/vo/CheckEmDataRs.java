package com.anyi.sparrow.organize.employee.vo;

import com.anyi.common.employee.domain.Employee;
import lombok.Data;

@Data
public class CheckEmDataRs {
    /**
     * 设置成新部门管理员的员工
     */
    private Employee employee;

    /**
     * 是否需要创建该员工
     */
    private boolean createEm;

    /**
     * 是否需要更改该员工的权限->管理员
     */
    private boolean changeEmRole;
}
