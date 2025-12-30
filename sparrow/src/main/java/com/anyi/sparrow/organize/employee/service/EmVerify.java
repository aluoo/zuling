package com.anyi.sparrow.organize.employee.service;

import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.organize.employee.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmVerify {
    @Autowired
    private EmService emService;
    @Autowired
    private DeptProcessService deptProcessService;
    @Autowired
    private EmployeeDao employeeDao;

    public boolean verifyManger(LoginUser user, Long childId) {
        Long id = employeeDao.verifyManger(user, childId);
        return id != null;
    }
}
