package com.anyi.sparrow.organize.employee.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.company.mapper.CompanyMapper;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.common.dept.mapper.DeptMapper;
import com.anyi.common.company.domain.Company;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.employee.domain.Employee;
import com.anyi.sparrow.organize.employee.enums.DeptType;
import com.anyi.sparrow.organize.employee.vo.EmployeeQueryVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/22
 */
@Slf4j
@Service
public class EmployeeSearchService {
    @Autowired
    private DeptProcessService deptProcessService;
    @Autowired
    private EmService emService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private CompanyProcessService companyProcessService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private DeptMapper deptMapper;

    public List<EmployeeQueryVO> queryChildrenByMobile(String mobile) {
        List<EmployeeQueryVO> resp = new ArrayList<>();
        Employee targetEmp = emService.getEmByMobile(mobile);
        if (Objects.isNull(targetEmp)) {
            return resp;
        }
        LoginUser loginUser = LoginUserContext.getUser();
        if (!targetEmp.getDeptCode().startsWith(loginUser.getDeptCode())) {
            // 非自己部门底下的员工不可搜索出来
            return resp;
        }
        String loginUserId = loginUser.getId() + ",";
        String sub = StrUtil.sub(targetEmp.getAncestors(), StrUtil.indexOfIgnoreCase(targetEmp.getAncestors(), loginUserId), targetEmp.getAncestors().length());
        List<String> split = StrUtil.split(sub, ",");
        List<Long> ancestoreIdList = new ArrayList<>();
        for (String s : split) {
            ancestoreIdList.add(Long.valueOf(s));
        }
        ancestoreIdList = ancestoreIdList.stream().sorted(Collections.reverseOrder()).filter(o -> !Objects.equals(o, loginUser.getId())).collect(Collectors.toList());
        List<Employee> children = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, ancestoreIdList).orderByDesc(Employee::getLevel));

        // 目标员工到登录账号之间的链条
        // 目标员工 -> 员工上级 -> 上级的上级。。。直到登录账号为止，排除掉登录账号
        if (CollUtil.isEmpty(children)) {
            return resp;
        }
        List<Long> deptIds = children.stream().map(Employee::getDeptId).collect(Collectors.toList());
        List<Dept> deptList = deptMapper.selectList(new LambdaQueryWrapper<Dept>().in(Dept::getId, deptIds));

        List<Long> companyIds = children.stream().map(Employee::getCompanyId).collect(Collectors.toList());
        List<Company> companies = companyMapper.selectList(new LambdaQueryWrapper<Company>().in(Company::getId, companyIds));
        Map<Long, Company> companyMap = buildCompanyMap(companies);
        Map<Long, Dept> deptMap = buildDeptMap(deptList);

        Map<Long, Employee> managers = new HashMap<>(1);
        for (Dept o : deptList) {
            Employee manager = emService.getManager(o);
            if (manager != null) {
                managers.putIfAbsent(manager.getId(), manager);
            }
        }

        for (Employee emp : children) {
            EmployeeQueryVO vo = EmployeeQueryVO.builder()
                    .id(emp.getId())
                    .deptId(emp.getDeptId())
                    .type(3)//1渠道 2部门 3人员
                    .name(emp.getName())
                    .mobileNumber(emp.getMobileNumber())
                    .isManager(false)
                    .isUpdate(false)
                    .isSelf(emp.getId().equals(loginUser.getId()))
                    .build();
            Dept dept = deptMap.get(emp.getDeptId());
            if (DeptType.MANGER.getCode() == dept.getType()) {
                Company company = companyMap.get(emp.getCompanyId());
                vo.setDeptName(company.getName());
            } else {
                vo.setDeptName(dept.getName());
            }
            if (DeptType.COMMON.getCode() == dept.getType()) {
                vo.setIsUpdate(true);
            }
            Employee manager = managers.get(emp.getId());
            if (manager != null) {
                vo.setIsManager(true);
            }
            resp.add(vo);
        }
        return resp;
    }

    private Map<Long, Company> buildCompanyMap(List<Company> companies) {
        return CollUtil.isEmpty(companies) ? new HashMap<>(1) : companies.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
    }

    private Map<Long, Dept> buildDeptMap(List<Dept> deptList) {
        return CollUtil.isEmpty(deptList) ? new HashMap<>(1) : deptList.stream().collect(Collectors.toMap(Dept::getId, Function.identity()));
    }
}