package com.anyi.common.dept.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.dept.mapper.DeptMapper;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.employee.vo.AgencyVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class DeptService extends ServiceImpl<DeptMapper, Dept> {
}