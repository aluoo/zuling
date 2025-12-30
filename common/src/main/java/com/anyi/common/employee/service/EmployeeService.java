package com.anyi.common.employee.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.service.IEmployeeAccountService;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.dto.EmployeeApplyDTO;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.company.req.CompanyReq;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.company.vo.AgencyCompanyVO;
import com.anyi.common.constant.Constants;
import com.anyi.common.dept.service.DeptService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.employee.vo.AgencyCalVO;
import com.anyi.common.employee.vo.AgencyVO;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.PasswordUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> {

    @Autowired
    DeptService deptService;
    @Autowired
    SnowflakeIdService snowflakeIdService;
    @Autowired
    IEmployeeAccountService employeeAccountService;
    @Autowired
    @Lazy
    CompanyService companyService;

    public Map<Long, Employee> getEmployeeInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<Employee> list = this.lambdaQuery().in(Employee::getId, ids).list();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
    }

    public Employee getByMobileStatus(String mobile) {
        List<Employee> employeeList = this.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getMobileNumber, mobile)
                .in(Employee::getStatus, Arrays.asList(EmStatus.NORMAL.getCode(), EmStatus.FREEZE.getCode())));
        if (CollUtil.isNotEmpty(employeeList)) {
            return employeeList.get(0);
        }
        return null;
    }

    public Employee getRecyclerByMobileStatus(String mobile) {
        List<Employee> employeeList = this.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getMobileNumber, mobile)
                .eq(Employee::getCompanyType, CompanyType.RECYCLE.getCode())
                .in(Employee::getStatus, Arrays.asList(EmStatus.NORMAL.getCode(), EmStatus.FREEZE.getCode())));
        if (CollUtil.isNotEmpty(employeeList)) {
            return employeeList.get(0);
        }
        return null;
    }

    /**
     * 代理一级界面接口
     */
    public AgencyVO agencyFirst(String ancestors, int currentLevel) {

        Employee current = this.getOne(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getAncestors,ancestors).eq(Employee::getLevel,currentLevel));

        //当前登陆人下级的代理
        List<Employee> employeeList = this.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, currentLevel + 1)
                .eq(Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .likeRight(Employee::getAncestors, ancestors)
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        if (CollUtil.isEmpty(employeeList)) {
            return AgencyVO.builder().build();
        }
        List<AgencyVO.AgencyChildVO> childVOList = new ArrayList<>();
        for (Employee employee : employeeList) {
            AgencyCalVO agencyCalVO = this.baseMapper.getByDeptCode(employee.getAncestors());
            AgencyVO.AgencyChildVO childVO = AgencyVO.AgencyChildVO.builder()
                    .deptName(deptService.getById(employee.getDeptId()).getName())
                    .employeeId(employee.getId())
                    .employeeName(employee.getName())
                    .status(employee.getStatus().intValue())
                    .statusName(EmStatus.getNameByCode(employee.getStatus()))
                    .phone(employee.getMobileNumber())
                    .staffNum(Optional.ofNullable(agencyCalVO).map(AgencyCalVO::getStaffNum).orElse(0))
                    .companyNum(Optional.ofNullable(agencyCalVO).map(AgencyCalVO::getCompanyNum).orElse(0))
                    .ancestors(employee.getAncestors())
                    .nextAgency(nextAgency(employee.getAncestors(), employee.getLevel()))
                    .nextCompany(nextCompany(employee.getAncestors(), employee.getLevel()))
                    .level(employee.getLevel()).build();

            childVOList.add(childVO);
        }
        AgencyVO vo = AgencyVO.builder().employeeNum(employeeList.size()).agencyList(childVOList).build();

        //直营门店数目
        CompanyReq req = new CompanyReq();
        req.setAplId(current.getId());
        List<AgencyCompanyVO> companyList = companyService.dictCompany(req);
        vo.setCompanyNum(CollUtil.isNotEmpty(companyList)?companyList.size():0);
        return vo;
    }

    /**
     * 下一步是否是代理
     *
     * @param ancestors
     * @param currentLevel
     * @return
     */
    private Boolean nextAgency(String ancestors, int currentLevel) {
        List<Employee> employeeList = this.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, currentLevel + 1)
                .likeRight(Employee::getAncestors, ancestors)
                .eq(Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        if (CollUtil.isEmpty(employeeList)) {
            return false;
        }
        return true;
    }

    /**
     * 下一步门店
     *
     * @param ancestors
     * @param currentLevel
     * @return
     */
    private Boolean nextCompany(String ancestors, int currentLevel) {
        List<Employee> employeeList = this.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getLevel, currentLevel + 1)
                .likeRight(Employee::getAncestors, ancestors)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.STORE.getCode(), CompanyType.CHAIN.getCode()))
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()));
        if (CollUtil.isEmpty(employeeList) || nextAgency(ancestors, currentLevel)) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前部门底下正常营业的门店管理员
     *
     * @param ancestors
     * @return
     */
    public List<Long> getAncestorsEmployee(String ancestors) {
        List<Employee> employeeList = this.list(Wrappers.lambdaQuery(Employee.class)
                .likeRight(Employee::getAncestors, ancestors)
                .eq(Employee::getDeptType, 1)
                .eq(Employee::getType, 1)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.STORE.getCode(), CompanyType.CHAIN.getCode())));
        if (CollUtil.isEmpty(employeeList)) {
            return null;
        }
        List<Long> employeeIds = employeeList.stream().map(Employee::getId).collect(Collectors.toList());
        return employeeIds;
    }

    public List<Employee> queryChildEmployees(long employeeId) {
        Employee employee = getById(employeeId);
        int level = employee.getLevel();
        boolean isAdmin = 0 == employee.getLevel();
        int childLevel = level + 1;
        String ancestor = employee.getAncestors();
        return lambdaQuery()
                .eq(Employee::getLevel, childLevel)
                .in(isAdmin, Employee::getType, Arrays.asList(EmType.MANGER_MANGER.getCode(), EmType.CM_MANGER.getCode()))
                .in(isAdmin, Employee::getCompanyType, CompanyType.COMPANY.getCode())
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                .likeRight(Employee::getAncestors, ancestor)
                .list();
    }

    @Transactional(rollbackFor = Exception.class)
    public String createEmployee(EmployeeApplyDTO applyDTO) {
        if (StrUtil.isBlank(applyDTO.getMobileNumber())) {
            throw new BusinessException(99999, "手机号不能为空");
        }
        if (StrUtil.isBlank(applyDTO.getName())) {
            throw new BusinessException(99999, "姓名不能为空");
        }
        if (!Validator.isMobile(applyDTO.getMobileNumber())) {
            throw new BusinessException(99999, "请输入正确的手机号");
        }
        Employee parentEmployee = this.getById(applyDTO.getEmployeeId());
        if (ObjectUtil.isNull(parentEmployee)) {
            throw new BusinessException(99999, "邀请人不存在");
        }

        if (getByMobileStatus(applyDTO.getMobileNumber()) != null) {
            throw new BusinessException(BizError.USER_EXIST);
        }

        Employee custom = new Employee();
        custom.setId(snowflakeIdService.nextId());
        custom.setCompanyId(parentEmployee.getCompanyId());
        custom.setCompanyType(parentEmployee.getCompanyType());
        custom.setCreateTime(new Date());
        custom.setCreator(parentEmployee.getName());
        custom.setDeptCode(parentEmployee.getDeptCode());
        custom.setDeptType(parentEmployee.getDeptType());
        custom.setType(2);
        custom.setUpdator(parentEmployee.getName());
        custom.setStatus(EmStatus.NORMAL.getCode());
        custom.setUpdateTime(new Date());
        custom.setMobileNumber(applyDTO.getMobileNumber());
        custom.setName(applyDTO.getName());
        custom.setDeptId(parentEmployee.getDeptId());
        // 设置人员组织关系
        custom.setAncestors(parentEmployee.getAncestors() + "," + custom.getId());
        custom.setIsLeaf(Boolean.TRUE);
        custom.setLevel(parentEmployee.getLevel() + 1);
        custom.setPassword(PasswordUtil.buildDefaultPassword(applyDTO.getMobileNumber()));
        this.save(custom);

        parentEmployee.setIsLeaf(Boolean.FALSE);
        parentEmployee.setUpdateTime(new Date());
        this.updateById(parentEmployee);

        EmployeeAccount employeeAccount = new EmployeeAccount();
        employeeAccount.setId(snowflakeIdService.nextId());
        employeeAccount.setEmployeeId(custom.getId());
        employeeAccount.setAncestors(custom.getAncestors());
        employeeAccountService.save(employeeAccount);

        Company company = companyService.getById(parentEmployee.getCompanyId());
        return Optional.ofNullable(company).map(Company::getName).orElse(null);
    }

    /**
     * 获取代理ID
     * @return
     */
    public Long getAgent(Long employeeId){
        Employee employee = this.getById(employeeId);
        Company company = companyService.getById(employee.getCompanyId());
        if(company.getPId().equals(Constants.LAN_HAI_CMP_ID)){
            return company.getAplId();
        }

        Company parentCompany = companyService.getById(company.getPId());
        return parentCompany.getAplId();

    }

    /**
     * 获取员工的下级正常员工
     * @param employeeId
     * @return
     */
    public List<Employee> queryNormalChild(long employeeId) {
        Employee employee = getById(employeeId);
        String ancestor = employee.getAncestors();
        return lambdaQuery()
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                .likeRight(Employee::getAncestors, ancestor)
                .list();
    }

}