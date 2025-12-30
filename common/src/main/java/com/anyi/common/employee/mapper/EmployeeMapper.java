package com.anyi.common.employee.mapper;

import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.domain.EmployeeExample;
import com.anyi.common.employee.vo.AgencyCalVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface EmployeeMapper extends BaseMapper<Employee> {
    long countByExample(EmployeeExample example);

    int deleteByExample(EmployeeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    int insertSelective(Employee record);

    List<Employee> selectByExampleWithRowbounds(EmployeeExample example, RowBounds rowBounds);

    List<Employee> selectByExample(EmployeeExample example);

    Employee selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByExample(@Param("record") Employee record, @Param("example") EmployeeExample example);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);

    void updateIsLeaf(@Param("id") Long id, @Param("isLeaf") Boolean isLeaf);

    /**
     * 从人员层级中查找 能开票的渠道层级
     *
     * @param ancestorEmployees
     * @return
     */
    Integer selectInvoiceAbleLevel(@Param("employees") List<Employee> ancestorEmployees);

    Employee selectBdByAncestors(String ancestors);

    List<Employee> selectChildrenByAncestors(@Param("targetId") Long targetId, @Param("targetDeptCode") String targetDeptCode);

    AgencyCalVO getByDeptCode(String ancestors);



}