package com.anyi.sparrow.organize.employee.dao;

import com.anyi.sparrow.organize.employee.domain.EmpCfg;
import com.anyi.sparrow.organize.employee.domain.EmpCfgExample;
import com.anyi.sparrow.organize.employee.dao.mapper.EmpCfgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmpCfgDao {
    @Autowired
    private EmpCfgMapper mapper;
    public EmpCfg selectByEmp(Long id) {
        EmpCfgExample example = new EmpCfgExample();
        example.createCriteria().andEmpIdEqualTo(id);
        List<EmpCfg> empCfgs = mapper.selectByExample(example);
        if (empCfgs.size() > 0){
            return empCfgs.get(0);
        }
        return null;
    }
}
