package com.anyi.sparrow.organize.employee.dao;

import com.anyi.common.company.domain.Company;
import com.anyi.common.company.domain.CompanyExample;
import com.anyi.common.company.mapper.CompanyMapper;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.domain.EmployeeExample;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.company.enums.CompanyStatus;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.company.mapper.ExtCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class CompanyDao {
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private EmployeeMapper emMapper;
    @Autowired
    private ExtCompanyMapper extCompanyMapper;

    public Company getByName(String name) {
        CompanyExample example = new CompanyExample();
        example.createCriteria().andNameEqualTo(name).andStatusIn(Arrays.asList(CompanyStatus.NORMAL.getCode(), CompanyStatus.FREEZE.getCode()));
        List<Company> companies = companyMapper.selectByExample(example);
        if (companies.size() > 0){
            return companies.get(0);
        }
        return null;
    }

    public Employee getManager(Long id) {
        EmployeeExample example = new EmployeeExample();
        example.createCriteria().andCompanyIdEqualTo(id)
                .andStatusEqualTo(EmStatus.NORMAL.getCode())
                .andTypeEqualTo(EmType.MANGER_MANGER.getCode());
        List<Employee> employees = emMapper.selectByExample(example);
        if (employees.size() > 0){
            return employees.get(0);
        }
        return null;
    }

    public int selectMaxCode(Long pId) {
        Integer maxCode = extCompanyMapper.selectMaxCode(pId);
        if (maxCode != null){
            return maxCode;
        }
        return 0;
    }

    public void deleteChannel(String code, String updator) {
        Company company = new Company();
        company.setStatus(CompanyStatus.CANCEL.getCode());
        company.setUpdator(updator);
        company.setUpdateTime(new Date());
        extCompanyMapper.deleteChannel(code, company);
    }

    public Company getCompanyByEmpId(Long empId) {
        Employee employee = emMapper.selectByPrimaryKey(empId);
        if(employee != null) {
            return companyMapper.selectById(employee.getCompanyId());
        }
        return null;
    }

    public void updatePdept(Long pDeptId, List<Long> cmpIds){
        extCompanyMapper.updatePdept(pDeptId, cmpIds);
    }

    public List<Company> getListByContactMobile(String mobile) {
        CompanyExample example = new CompanyExample();
        example.createCriteria().andContactMobileEqualTo(mobile).andStatusIn(Arrays.asList(CompanyStatus.TO_AUDIT.getCode(),CompanyStatus.NORMAL.getCode(), CompanyStatus.FREEZE.getCode()));
        List<Company> companies = companyMapper.selectByExample(example);
        return companies;

    }

    public List<Long> queryAutoSettleCompanyIds() {
        return companyMapper.queryAutoSettleCompanyIds();
    }
}
