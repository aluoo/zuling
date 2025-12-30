package com.anyi.miniapp.company.service;

import cn.hutool.core.util.ObjectUtil;
import com.anyi.common.account.req.CancelEmployeeReq;
import com.anyi.common.account.req.UpdateEmployeeReq;
import com.anyi.common.account.vo.CompanyEmployeeVO;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.service.CompanyService;
import com.anyi.common.constant.CacheDefine;
import com.anyi.common.constant.Constants;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.domain.UserLogin;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.employee.mapper.UserLoginMapper;
import com.anyi.common.employee.service.EmployeeService;
import com.anyi.common.product.domain.request.RecyclerQuoteLogListReq;
import com.anyi.common.product.domain.response.RecyclerQuoteCountInfoVO;
import com.anyi.common.product.service.OrderQuotePriceLogService;
import com.anyi.miniapp.interceptor.UserContextHolder;
import com.anyi.miniapp.interceptor.UserManager;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class RecycleCompanyService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public CompanyEmployeeVO getCompanyEmployee(Long companyId, Long employeeId) {
        CompanyEmployeeVO resultVo = new CompanyEmployeeVO();
        Company company = companyService.getById(companyId);
        Employee employee = employeeService.getById(employeeId);
        resultVo.setEmployeeId(employee.getId());
        resultVo.setEmployeeName(employee.getName());
        resultVo.setEmployeePhone(employee.getMobileNumber());
        resultVo.setFrontUrl(company.getFrontUrl());
        resultVo.setManage(company.getEmployeeId().equals(employee.getId()));
        resultVo.setCompanyName(company.getName());
        return resultVo;
    }

    public List<Employee> getEmployeeList(Long companyId) {
        List<Employee> resultList = employeeService.list(Wrappers.lambdaQuery(Employee.class)
                .eq(Employee::getCompanyId, companyId)
                .eq(Employee::getStatus, EmStatus.NORMAL.getCode()).eq(Employee::getType, EmType.MANGER_EM.getCode()));
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(CancelEmployeeReq req) {
        Employee employee = employeeService.getById(req.getEmployeeId());
        if (employee == null || employee.getStatus() != EmStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        employee.setStatus(EmStatus.CANCEL.getCode());
        employee.setUpdateTime(new Date());
        employee.setUpdator(UserManager.getUserName());
        employeeService.updateById(employee);
        //删除登录缓存
        Long userId = UserManager.getUserId();
        UserLogin userLogin = userLoginMapper.selectById(userId);
        String userKey = CacheDefine.UserToken.getUserKey(userLogin.getToken());
        redisTemplate.delete(userKey);
        //删除登录记录
        userLoginMapper.deleteById(userId);
        //清除本地LOCAL
        UserContextHolder.getInstance().clear();
    }


    public void updateEmployee(UpdateEmployeeReq req) {
        Employee employee = employeeService.getById(req.getEmployeeId());
        if (employee == null || employee.getStatus() != EmStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        employee.setName(req.getName());
        employee.setUpdateTime(new Date());
        employee.setUpdator(UserManager.getUserName());
        employeeService.updateById(employee);
    }

}