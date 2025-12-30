package com.anyi.sparrow.organize.employee.dao;

import com.anyi.common.dept.domain.Dept;
import com.anyi.common.dept.domain.DeptExample;
import com.anyi.sparrow.organize.employee.enums.DeptStatus;
import com.anyi.sparrow.organize.employee.enums.DeptType;
import com.anyi.common.dept.mapper.DeptMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.ExtDeptMapper;
import com.anyi.sparrow.organize.employee.vo.DeptListRs;
import com.anyi.sparrow.organize.employee.vo.DeptListTotalRs;
import com.anyi.sparrow.base.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class DeptDao {
    @Autowired
    private ExtDeptMapper extDeptMapper;
    @Autowired
    private DeptMapper deptMapper;

    public List<DeptListRs> getAllChildDepts(Long companyId, String deptCode) {
        return extDeptMapper.getAllChildDepts(companyId, deptCode);
    }

    public List<DeptListRs> getAllStatusChildDepts(Long companyId, String deptCode) {
        return extDeptMapper.getAllStatusChildDepts(companyId, deptCode);
    }

    public List<DeptListRs> getChildDepts(Long companyId, Long deptId) {
        return extDeptMapper.getChildDepts(companyId, deptId);
    }

    public Dept getByName(Long companyId, String name) {
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria cri = deptExample.createCriteria();
        cri.andNameEqualTo(name).andStatusIn(Arrays.asList(DeptStatus.NORMAL.getCode(), DeptStatus.FREEZE.getCode()));
        cri.andCompanyIdEqualTo(companyId);
        List<Dept> depts = deptMapper.selectByExample(deptExample);
        if (depts.size() > 0){
            return depts.get(0);
        }
        return null;
    }

    public Integer selectMaxCode(Long pdeptId) {
        Integer i = extDeptMapper.selectMaxCode(pdeptId);
        if (i == null){
            i = 0;
        }
        return i;
    }

    public int queryTotalEm(String deptCode) {
        String childDeptCode = deptCode + "-";
        return extDeptMapper.queryTotalEm(deptCode, childDeptCode);
    }

    public Dept getManagerDept(Long companyId) {
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andCompanyIdEqualTo(companyId)
                .andStatusEqualTo(DeptStatus.NORMAL.getCode())
                .andTypeEqualTo(DeptType.MANGER.getCode());
        List<Dept> depts = deptMapper.selectByExample(deptExample);
        if (depts.size() > 0){
            return depts.get(0);
        }
        return null;
    }

    public void deleteChannel(String code, LoginUser user) {
        Dept dept = new Dept();
        dept.setUpdator(user.getName());
        dept.setUpdateTime(new Date());
        dept.setStatus(DeptStatus.CANCEL.getCode());
        extDeptMapper.deleteChannel(code, dept);
    }

    public List<DeptListTotalRs> getChildDeptsAndTotal(Long companyId, Long deptId) {
        return extDeptMapper.getChildDeptsAndTotal(companyId, deptId);
    }

   public void updateChildsCode(String newPcode, String oldPcode, List cmpIds, Date updateTime, String updator){
        extDeptMapper.updateChildsCode(newPcode, oldPcode, cmpIds, updateTime, updator);
   }

   public List<Long> queryCmpIdByCode(String code){
        return extDeptMapper.queryCmpIdByCode(code);
   }

    public int queryTotalEmAll(Long deptId, String code, Integer companyType) {
        return extDeptMapper.queryTotalEmAll(deptId, code + "-",  companyType);
    }

    public Dept getManagerDeptAll(Long companyId) {
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andCompanyIdEqualTo(companyId)
                .andTypeEqualTo(DeptType.MANGER.getCode());
        List<Dept> depts = deptMapper.selectByExample(deptExample);
        if (depts.size() > 0){
            return depts.get(0);
        }
        return null;
    }
}
