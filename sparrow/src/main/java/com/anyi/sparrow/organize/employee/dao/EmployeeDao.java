package com.anyi.sparrow.organize.employee.dao;

import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.domain.EmployeeExample;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.sparrow.organize.employee.dao.mapper.ExtEmMapper;
import com.anyi.sparrow.organize.employee.vo.EmInfo;
import com.anyi.sparrow.base.security.LoginUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDao {
    @Autowired
    private EmployeeMapper mapper;
    @Autowired
    private ExtEmMapper extEmMapper;

    public Employee getById(long id) {
        return mapper.selectByPrimaryKey(id);
    }

    public Employee getByMobile(String mobile) {
        return getByMobile(mobile, Collections.singletonList(EmStatus.NORMAL.getCode()));
    }

    public Employee getByMobile(String mobile, List<Byte> status) {
        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andMobileNumberEqualTo(mobile).andStatusIn(status);
        List<Employee> records = mapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }
    public Employee getByMobileAll(String mobile) {
        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andMobileNumberEqualTo(mobile);
        List<Employee> records = mapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }


    public Long verifyManger(LoginUser user, Long childId) {
        return extEmMapper.verifyManager(user, childId);
    }

    public void deleteChannel(String code, LoginUser user) {
        Employee employee = new Employee();
        employee.setStatus(EmStatus.CANCEL.getCode());
        employee.setUpdator(user.getName());
        employee.setUpdateTime(new Date());
        extEmMapper.deleteChannel(code, employee);
    }

    public List<Employee> getEmployees(Long deptId){
        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andDeptIdEqualTo(deptId).andStatusEqualTo(EmStatus.NORMAL.getCode());
        List<Employee> employees = mapper.selectByExample(example);
        return employees;
    }

    public Employee getManager(Long deptId, int emType) {
        EmployeeExample exp = new EmployeeExample();
        exp.createCriteria().andDeptIdEqualTo(deptId).andTypeEqualTo(emType).andStatusEqualTo(EmStatus.NORMAL.getCode());
        exp.setOrderByClause("id asc limit 1");
        List<Employee> employees = mapper.selectByExample(exp);
        if (CollectionUtils.isNotEmpty(employees)){
            return employees.get(0);
        }else{
            return null;
        }
    }

    public List<EmInfo> queryByNameOrMobile(String express, LoginUser user, Dept parent, boolean hadManger) {
        return extEmMapper.queryByNameOrMobile(express, user, parent, hadManger);
    }

    public void updateDeptCode(List cmpIds, String deptCode, Date updateTime, String updator){
        extEmMapper.updateDeptCode(cmpIds, deptCode, updateTime, updator);
    }

    public List<Map<String, String>> queryTokenByDeptCode(List cmpIds, String deptCode, Date currDate){
        return extEmMapper.queryTokenByDeptCode(cmpIds, deptCode, currDate);
    }

}

