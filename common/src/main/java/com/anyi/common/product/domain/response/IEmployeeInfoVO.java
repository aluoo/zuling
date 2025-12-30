package com.anyi.common.product.domain.response;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.employee.domain.Employee;

import java.util.Map;
import java.util.Optional;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/14
 * @Copyright
 * @Version 1.0
 */
public interface IEmployeeInfoVO {
    Long getStoreEmployeeId();
    Long getRecyclerEmployeeId();
    void setStoreEmployeeId(Long storeEmployeeId);
    void setRecyclerEmployeeId(Long recyclerEmployeeId);
    String getStoreEmployeeName();
    String getStoreEmployeeMobile();
    String getRecyclerEmployeeName();
    String getRecyclerEmployeeMobile();
    void setStoreEmployeeName(String storeEmployeeName);
    void setStoreEmployeeMobile(String storeEmployeeMobile);
    void setRecyclerEmployeeName(String recyclerEmployeeName);
    void setRecyclerEmployeeMobile(String recyclerEmployeeMobile);

    default void setEmployeeInfo(Map<Long, Employee> map) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        Optional<Employee> storeEmployee = Optional.empty();
        Optional<Employee> recyclerEmployee = Optional.empty();
        if (this.getStoreEmployeeId() != null) {
            storeEmployee = Optional.ofNullable(map.get(this.getStoreEmployeeId()));
        }
        if (this.getRecyclerEmployeeId() != null) {
            recyclerEmployee = Optional.ofNullable(map.get(this.getRecyclerEmployeeId()));
        }
        String storeEmployeeName = storeEmployee.map(Employee::getName).orElse(null);
        String storeEmployeeMobile = storeEmployee.map(Employee::getMobileNumber).orElse(null);

        String recyclerEmployeeName = recyclerEmployee.map(Employee::getName).orElse(null);
        String recyclerEmployeeMobile = recyclerEmployee.map(Employee::getMobileNumber).orElse(null);

        this.setStoreEmployeeName(storeEmployeeName);
        this.setStoreEmployeeMobile(storeEmployeeMobile);
        this.setRecyclerEmployeeName(recyclerEmployeeName);
        this.setRecyclerEmployeeMobile(recyclerEmployeeMobile);
    }
}