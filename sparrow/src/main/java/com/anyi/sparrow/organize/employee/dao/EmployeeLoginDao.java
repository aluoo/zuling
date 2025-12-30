package com.anyi.sparrow.organize.employee.dao;

import com.anyi.sparrow.organize.employee.domain.EmployeeLogin;
import com.anyi.sparrow.organize.employee.domain.EmployeeLoginExample;
import com.anyi.sparrow.organize.employee.dao.mapper.EmployeeLoginMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.ExtEmployeeLoginMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeLoginDao {
    @Autowired
    private EmployeeLoginMapper mapper;

    @Autowired
    private ExtEmployeeLoginMapper extMapper;

    public EmployeeLogin getByToken(String token) {
        EmployeeLoginExample example = new EmployeeLoginExample();
        example.createCriteria().andTokenEqualTo(token);
        List<EmployeeLogin> records = mapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }

    public EmployeeLogin getByUserId(long userId) {
        EmployeeLoginExample example = new EmployeeLoginExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<EmployeeLogin> records = mapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }

    public int insert(EmployeeLogin record) {
        return mapper.insertSelective(record);
    }

    public int update(EmployeeLogin record) {
        EmployeeLoginExample example = new EmployeeLoginExample();
        example.createCriteria().andUserIdEqualTo(record.getUserId());
        return mapper.updateByExampleSelective(record, example);
    }

    public int delete(Long employeeId) {
        EmployeeLoginExample example = new EmployeeLoginExample();
        example.createCriteria().andUserIdEqualTo(employeeId);
        return mapper.deleteByExample(example);
    }

//    public int deleteByCmpId(Long cmpId) {
//        return extMapper.deleteByCmpId(cmpId);
//    }
//
//    public List<EmployeeLogin> selectByCmpId(Long cmpId) {
//        return extMapper.selectByCmpId(cmpId);
//    }

    public int deleteByDeptCode(String deptCode) {
        return extMapper.deleteByDeptCode(deptCode);
    }

    public List<EmployeeLogin> selectByCode(String deptCode) {
        return extMapper.selectByDeptCode(deptCode);
    }
}
