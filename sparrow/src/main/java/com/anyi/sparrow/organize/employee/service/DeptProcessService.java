package com.anyi.sparrow.organize.employee.service;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.employee.dao.DeptDao;
import com.anyi.common.dept.mapper.DeptMapper;
import com.anyi.common.company.domain.Company;
import com.anyi.common.dept.domain.Dept;
import com.anyi.sparrow.organize.employee.enums.DeptStatus;
import com.anyi.sparrow.organize.employee.enums.DeptType;
import com.anyi.sparrow.organize.employee.vo.DeptListRs;
import com.anyi.sparrow.organize.employee.vo.DeptListTotalRs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DeptProcessService {
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    private static int retrySaveDetpTimes = 3;

    private static final Logger logger = LoggerFactory.getLogger(DeptProcessService.class);

    public List<DeptListRs> getAllChildDepts(Long companyId, String deptCode) {
        return deptDao.getAllChildDepts(companyId, deptCode + "-");
    }

    public List<DeptListRs> getAllStatusChildDepts(Long companyId, String deptCode) {
        return deptDao.getAllStatusChildDepts(companyId, deptCode + "-");
    }

    public List<DeptListRs> getAllChildDeptsWithOwn(Long companyId, String deptCode, Long ownId) {
        List<DeptListRs> depts = deptDao.getAllChildDepts(companyId, deptCode + "-");
        Dept dept = deptMapper.selectByPrimaryKey(ownId);
        DeptListRs own = new DeptListRs();
        BeanUtils.copyProperties(dept, own);
        depts.add(0, own);
        return depts;
    }

    public List<DeptListRs> getChildDepts(Long companyId, Long deptId) {
        return deptDao.getChildDepts(companyId, deptId);
    }

    public List<DeptListTotalRs> getChildDeptsAndTotal(Long companyId, Long deptId) {
        return deptDao.getChildDeptsAndTotal(companyId, deptId);
    }

    public Dept getById(Long deptId) {
        return deptMapper.selectByPrimaryKey(deptId);
    }

    public Dept selectByName(Long companyId, String name) {
        return deptDao.getByName(companyId, name);
    }

    public boolean deptExist(Long companyId, String name) {
        return selectByName(companyId, name) != null;
    }

    public String generateCode(Long pdeptId, String pdeptCode) {
        Integer newCode = deptDao.selectMaxCode(pdeptId);
        newCode++;
        return pdeptCode + "-" + newCode;
    }

    @Transactional
    public Dept saveDept(Dept parent, String name, String creator) {
        Dept dept = buildDept(parent, name, creator);
        int times = retrySaveDetpTimes;
        doSave(dept, parent.getId(), parent.getCode(), times);
        return dept;
    }

    private Dept buildDept(Dept parent, String name, String creator) {
        Dept toSave = new Dept();
        toSave.setCreateTime(new Date());
        toSave.setCompanyId(parent.getCompanyId());
        toSave.setCompanyType(parent.getCompanyType());
        toSave.setCreator(creator);
        toSave.setId(snowflakeIdService.nextId());
        toSave.setName(name);
        toSave.setpDeptId(parent.getId());
        toSave.setType(DeptType.COMMON.getCode());
        toSave.setUpdateTime(new Date());
        toSave.setUpdator(creator);
        toSave.setStatus(DeptStatus.NORMAL.getCode());
        return toSave;
    }

    private void doSave(Dept dept, Long parentId, String parentCode, int retryTimes) {
        try {
            dept.setCode(generateCode(parentId, parentCode));
            deptMapper.insert(dept);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            retryTimes--;
            if (retryTimes == 0){
                throw new BusinessException(BizError.CREATE_DEPT_ERROR);
            }
            //一旦出现bug就会死循环，需要设置一个重试的最大次数
            doSave(dept, parentId, parentCode, retryTimes);
        }
    }

    @Transactional
    public Dept createDeptOfChannel(Company data, LoginUser user) {
        Dept dept = buildDeptOfChannel(data, user);
        int times =  retrySaveDetpTimes;
        doSave(dept, user.getDeptId(), user.getDeptCode(), times);
        return dept;
    }

    private Dept buildDeptOfChannel(Company data, LoginUser user) {
        Dept toSave = new Dept();
        toSave.setCreateTime(new Date());
        toSave.setCompanyId(data.getId());
        toSave.setCompanyType(data.getType());
        toSave.setCreator(user.getName());
        toSave.setId(snowflakeIdService.nextId());
        toSave.setName(data.getName() + "-管理部");
        toSave.setpDeptId(user.getDeptId());
        toSave.setType(DeptType.MANGER.getCode());
        toSave.setUpdateTime(new Date());
        toSave.setUpdator(user.getName());
        toSave.setStatus(DeptStatus.NORMAL.getCode());
        return toSave;
    }

    public int queryTotalEm(String deptCode) {
        return deptDao.queryTotalEm(deptCode);
    }

   public void updateParent(Dept dept, Dept parent) {
        dept.setpDeptId(parent.getId());
        dept.setCode(generateCode(parent.getId(), parent.getCode()));
        dept.setUpdateTime(new Date());
        dept.setUpdator(LoginUserContext.getUser().getName());
        deptMapper.updateByPrimaryKey(dept);
    }

    public void update(Dept dept, String updator) {
        dept.setUpdateTime(new Date());
        dept.setUpdator(updator);
        deptMapper.updateByPrimaryKey(dept);
    }

    @Transactional
    public void updateChildsCode(String newPcode, String oldPcode, List cmpIds){
        LoginUser loginUser = LoginUserContext.getUser();
        deptDao.updateChildsCode(newPcode, oldPcode, cmpIds, new Date(), loginUser.getName());
    }

    public Dept getManagerDept(Long companyId) {
        return deptDao.getManagerDept(companyId);
    }
    public Dept getManagerDeptAll(Long companyId) {
        return deptDao.getManagerDeptAll(companyId);
    }

    public void deleteChannel(String code, LoginUser user) {
        deptDao.deleteChannel(code, user);
    }

    public Dept getParentDept(Long deptId) {
        return deptMapper.selectByPrimaryKey(deptMapper.selectByPrimaryKey(deptId).getpDeptId());
    }

    public List<Long> queryCmpIdByCode(String code){
        return deptDao.queryCmpIdByCode(code);
    }

    public int queryTotalEmAll(Long deptId, String code, Integer companyType) {
        return deptDao.queryTotalEmAll(deptId, code, companyType);
    }
}
