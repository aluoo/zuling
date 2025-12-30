package com.anyi.sparrow.organize.employee.service;

import com.anyi.common.advice.BaseException;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.enums.CompanyStatus;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.PasswordUtil;
import com.anyi.sparrow.assist.system.dao.SysDictDao;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.organize.employee.dao.mapper.TemporaryEmployeeMapper;
import com.anyi.sparrow.organize.employee.domain.TemporaryEmployee;
import com.anyi.sparrow.organize.employee.enums.DeptStatus;
import com.anyi.sparrow.organize.employee.enums.DeptType;
import com.anyi.sparrow.organize.employee.enums.ObjectInfoType;
import com.anyi.sparrow.organize.employee.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmManagerService {
    @Autowired
    private DeptProcessService deptProcessService;
    @Autowired
    private EmService emService;
//    @Autowired
//    private SmsSender smsSender;
    @Autowired
    private CompanyProcessService companyProcessService;
    @Autowired
    private SysDictDao dictDao;
    @Autowired
    private TemporaryEmployeeMapper temporaryEmployeeMapper;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    private void validCreate() {
        String flag = Optional.ofNullable(dictDao.getByName("create_em_switch")).map(String::toString).orElse("0");
        boolean canNotCreate = flag.equals("0");
        if (canNotCreate) {
            throw new BaseException(-1, "当前无法创建");
        }
    }

    public EmployeeVO getEmployeeAdaptor(Long emId) {
        EmployeeVO vo = null;
        try {
            vo = getEmployee(emId);
            vo.setTemporaryUser(false);
        } catch (BusinessException e) {
            //
        }
        if (vo != null) {
            return vo;
        }
        // get temporary
        if (emId == null) {
            emId = LoginUserContext.getUser().getId();
        }
        TemporaryEmployee employee = temporaryEmployeeMapper.selectById(emId);
        if (employee == null || employee.getStatus() != EmStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);
        vo.setTemporaryUser(true);
        return vo;
    }

    public EmployeeVO getEmployee(Long emId) {
        if (emId == null) {
            emId = LoginUserContext.getUser().getId();
        }
        Employee employee = emService.getEmployee(emId);
        if (employee == null || employee.getStatus() != EmStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        EmployeeVO vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);
        if (employee.getType().equals(EmType.MANGER_MANGER.getCode()) || employee.getType().equals(EmType.CM_MANGER.getCode())) {
            vo.setIsUpdateDept(false);
            vo.setCommissionPlanWatchAble(true);
        } else {
            vo.setIsUpdateDept(true);
        }
        Company company = companyProcessService.getById(employee.getCompanyId());
        // 能开票的一级渠道才能查看团队收益
        /*if (company.getInvoiceAble() && vo.getType() == EmType.MANGER_MANGER.getCode() ){
            vo.setTeamBenefitsWatchAble(Boolean.TRUE);
        }*/
        //后台是否有设置对公
        vo.setTeamBenefitsWatchAble(employee.getPublicFlag());
        Dept dept = deptProcessService.getById(employee.getDeptId());
        vo.setCompanyName(company.getName());
        vo.setDeptName(dept.getName());
        vo.setCompanyType(company.getType());
        return vo;
    }

    public DeptVO getDept(Long deptId) {
        if (deptId == null) {
            deptId = LoginUserContext.getUser().getDeptId();
        }
        Dept dept = deptProcessService.getById(deptId);
        if (dept == null || dept.getStatus() != DeptStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.DEPT_NOT_EXIST);
        }
        Dept pdept = deptProcessService.getById(dept.getpDeptId());
        Employee manager = emService.getManager(dept);
        DeptVO vo = new DeptVO();
        BeanUtils.copyProperties(dept, vo);
        if (manager != null) {
            vo.setManagerName(manager.getName());
            vo.setManagerPhone(manager.getMobileNumber());
        }
        if (pdept != null) {
            vo.setPdeptId(pdept.getId());
            vo.setPdeptName(pdept.getName());
        }
        return vo;
    }

    public void updateHeadUrl(String headUrl) {
        Long id = LoginUserContext.getUser().getId();
        Employee employee = new Employee();
        employee.setId(id);
        employee.setHeadUrl(headUrl);
        emService.updateEm(employee);
    }

    public void updateName(String name) {
        Long id = LoginUserContext.getUser().getId();
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        emService.updateEm(employee);
    }
    public CompanyVO getCompany(Long companyId) {
        if (companyId == null) {
            companyId = LoginUserContext.getUser().getCompanyId();
        }
        Company company = companyProcessService.getById(companyId);
        if (company == null || company.getStatus() != CompanyStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        Employee manager = companyProcessService.getManager(company.getId());
        CompanyVO vo = new CompanyVO();
        BeanUtils.copyProperties(company, vo);
        if (manager != null) {
            vo.setManagerName(manager.getName());
            vo.setManagerPhone(manager.getMobileNumber());
        }
        Dept dept = deptProcessService.getById(company.getPDeptId());
        if (dept != null) {
            vo.setMangerDeptId(dept.getId());
            vo.setManagerDept(dept.getName());
        }
        return vo;
    }


    @Transactional
    public void createEm(String mobile, String name, Long deptId) {
        Dept dept = deptProcessService.getById(deptId);
        if (emService.getEmByMobile(mobile) != null) {
            throw new BusinessException(BizError.USER_EXIST);
        }
        /*if(LoginUserContext.getUser().getLevel()>1 && CompanyType.COMPANY.getCode() == LoginUserContext.getUser().getCompanyType().intValue()){
            throw new BusinessException(99999,"暂无创建员工权限");
        }*/
        int type = dept.getType() == DeptType.MANGER.getCode() ? EmType.MANGER_EM.getCode() : EmType.CM_EM.getCode();
//        createEm(mobile, name, dept, type);
        Employee parentEmployee = emService.getManager(dept);
        createEmNew(mobile, name, dept, type,parentEmployee);
    }


    @Transactional
    public void updateEm(UpdateEmReq emInfoReq) {
        LoginUser user = LoginUserContext.getUser();
        Employee employee = emService.getById(emInfoReq.getEmId());
        if (employee == null || employee.getStatus() != EmStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        //变更了手机号，新手机是未注册
        if (!employee.getMobileNumber().equals(emInfoReq.getMobileNumber())) {
            Employee emp = emService.getEmByMobile(emInfoReq.getMobileNumber());
            if (emp != null) {
                throw new BusinessException(BizError.USER_EXIST);
            }
        }
        employee.setUpdator(user.getName());
        employee.setUpdateTime(new Date());
        if (!employee.getName().equals(emInfoReq.getName()) || !employee.getMobileNumber().equals(emInfoReq.getMobileNumber())) {
            employee.setMobileNumber(emInfoReq.getMobileNumber());
            employee.setName(emInfoReq.getName());
            // 公司管理员修改管理员信息
            if (employee.getType() == EmType.MANGER_MANGER.getCode()) {
                Company company = companyProcessService.getById(employee.getCompanyId());
                company.setContact(emInfoReq.getName());
                company.setContactMobile(emInfoReq.getMobileNumber());
                company.setUpdateTime(new Date());
                company.setUpdator(user.getName());
                companyProcessService.updateCompany(company);
            }
            if (!employee.getMobileNumber().equals(emInfoReq.getMobileNumber())) {
                // 手机号变更，重置密码
                String newPwd = PasswordUtil.buildDefaultPassword(emInfoReq.getMobileNumber());
                employee.setPassword(newPwd);
            }
        }
        boolean modifyDept = false;
        //变更了部门
        if (!employee.getDeptId().equals(emInfoReq.getDeptId())) {
            throw new BusinessException(BizError.CANNOT_EDIT_EMPLOYEE_DEPT);
            /*modifyDept = true;
            Dept dept = deptService.getById(emInfoReq.getDeptId());
            employee.setDeptCode(dept.getCode());
            employee.setDeptType(dept.getType());
            employee.setDeptId(dept.getId());
            employee.setCompanyId(dept.getCompanyId());
            employee.setCompanyType(dept.getCompanyType());
            if (DeptType.MANGER.getCode() == dept.getType().intValue()) {
                employee.setType(EmType.MANGER_EM.getCode());
            } else {
                employee.setType(EmType.CM_EM.getCode());
            }
            //要求重新登录
            emService.logout(employee.getId());*/
        }
        //更新后台账号
     /*   updateManAccount(employee, modifyDept);*/
        emService.updateEm(employee);
    }

//    private void createEm(String mobile, String name, Dept dept, int type) {
//        String creator = LoginUserContext.getUser().getName();
//        Employee employee = buildEm(dept, creator, name, mobile, type);
//        // 创建员工、维护人员组织关系、初始化个人账户信息
//        emService.save(employee);
//        //createManAccount(employee);
//        // 发短信
//        smsSender.send(SmsMessage.create().mobile(mobile).templateCode(BizEnum.EM_Register.getTemplateCode()));
//    }


    private void createEmNew(String mobile, String name, Dept dept, int type,Employee parentEmployee ) {
        validCreate();
        String creator = LoginUserContext.getUser().getName();
        // 查找父级
//        Employee parentEmployee = emService.getManager(dept);

        Employee employee = buildEm(dept, creator, name, mobile, type, parentEmployee);
        // 创建员工、维护人员组织关系、初始化个人账户信息
        emService.save(employee);
        updateParentIsLeaf(parentEmployee);
        //createManAccount(employee);
        // 发短信
//        smsSender.send(SmsMessage.create().mobile(mobile).templateCode(BizEnum.EM_Register.getTemplateCode()));
    }

    private void updateParentIsLeaf(Employee parentEmployee){
        if (parentEmployee.getIsLeaf() ){
            emService.updateIsLeaf(parentEmployee,Boolean.FALSE);
        }
    }


    private Employee buildEm(Dept dept, String creator, String name, String mobile, int type, Employee parent) {
        Employee data = new Employee();
        data.setId(snowflakeIdService.nextId());
        data.setCompanyId(dept.getCompanyId());
        data.setCompanyType(dept.getCompanyType());
        data.setCreateTime(new Date());
        data.setCreator(creator);
        data.setDeptCode(dept.getCode());
        data.setDeptType(dept.getType());
        data.setType(type);
        data.setUpdator(creator);
        data.setStatus(EmStatus.NORMAL.getCode());
        data.setUpdateTime(new Date());
        data.setMobileNumber(mobile);
        data.setName(name);
        data.setDeptId(dept.getId());
        // 设置人员组织关系
        data.setAncestors(parent.getAncestors() + "," + data.getId());
        data.setIsLeaf(Boolean.TRUE);
        data.setLevel(parent.getLevel() + 1);
        String securePwd = PasswordUtil.buildDefaultPassword(mobile);
        data.setPassword(securePwd);
        return data;
    }


    public List<DeptListRs> queryDeptList() {
        LoginUser user = LoginUserContext.getUser();
        return deptProcessService.getAllChildDeptsWithOwn(user.getCompanyId(), user.getDeptCode(), user.getDeptId());
    }

    @Transactional
    public DeptRes createDept(CreateDeptReq req) {
        /*if(LoginUserContext.getUser().getLevel()>1 && CompanyType.COMPANY.getCode() == LoginUserContext.getUser().getCompanyType().intValue()){
            throw new BusinessException(99999,"暂无创建员工权限");
        }*/
        LoginUser user = LoginUserContext.getUser();
        if (req.getPdeptId() == null) {
            req.setPdeptId(user.getDeptId());
        }

        if (StringUtils.isEmpty(req.getManagerName())) {
            throw new BusinessException(BizError.Name_empty);
        }

        if (StringUtils.isEmpty(req.getMobile())) {
            throw new BusinessException(BizError.Mobile_EMPTY);
        }

        DeptRes deptRes = new DeptRes();
        if (deptProcessService.deptExist(user.getCompanyId(), req.getName())) {
            throw new BusinessException(BizError.DEPT_EXIST);
        }
        Dept parent = deptProcessService.getById(req.getPdeptId());
        Employee manager = emService.getManager(parent);
        if (manager == null) {
            throw new BusinessException(BizError.NO_MANAGER);
        }

        CheckEmDataRs checkEmDataRs = new CheckEmDataRs();
        if (StringUtils.hasLength(req.getMobile())) {
            checkEmDataRs = checkEmData(req, user, deptRes);
        }
        Dept toSave = deptProcessService.saveDept(parent, req.getName(), user.getName());
        // 创建员工
        if (checkEmDataRs.isCreateEm()) {
            createEmNew(req.getMobile(), req.getManagerName(), toSave, EmType.CM_MANGER.getCode(),manager);
        }
        // 修改员工角色
        if (checkEmDataRs.isChangeEmRole()) {
            doChangeEmRole(toSave, user, EmType.CM_MANGER, checkEmDataRs.getEmployee());
            //要求重新登录
            emService.logout(checkEmDataRs.getEmployee().getId());
        }
        return deptRes;
    }

    private CheckEmDataRs checkEmData(CreateDeptReq req, LoginUser user, DeptRes deptRes) {
        CheckEmDataRs checkEmDataRs = new CheckEmDataRs();
        Employee employee = emService.getEmByMobile(req.getMobile());
        checkEmDataRs.setEmployee(employee);
        //管理员是已存在的员工
        if (employee != null && employee.getStatus() == EmStatus.NORMAL.getCode()) {
            // 员工电话，姓名是否匹配
            if (!employee.getName().equals(req.getManagerName())) {
                throw new BusinessException(BizError.ID_NAME_NOT_MATCH);
            }
            boolean manage = false;
            // 是否有权限管理该员工
            //管理员可以管理本公司本部门员工(非自己)+下级部门员工
            if (user.getCompanyId().equals(employee.getCompanyId())) {
                if (user.getType().equals(EmType.MANGER_MANGER.getCode()) || user.getType().equals(EmType.CM_MANGER.getCode())) {
                    if (!user.getId().equals(employee.getId()) && (user.getDeptId().equals(employee.getDeptId())) || employee.getDeptCode().startsWith(user.getDeptCode() + "-")) {
                        manage = true;
                    }
                } else if (user.getType().equals(EmType.MANGER_EM.getCode())) {
                    //管理部门员工可以管理下级部门员工
                    if (employee.getDeptCode().startsWith(user.getDeptCode() + "-")) {
                        manage = true;
                    }
                }
            }
//           boolean mange = emVerify.verifyManger(user, employee.getId());
            if (!manage) {
                throw new BusinessException(BizError.EMP_POWER_ERROR);
            }
            // 是否是其他部门管理员
            if ((employee.getType().equals(EmType.CM_MANGER.getCode()) || employee.getType().equals(EmType.MANGER_MANGER.getCode()))) {
                //暂时不支持将其他部门管理员设置为新的管理员
                throw new BusinessException(BizError.REPLACE_MANAGER);
//                parent = deptService.getById(employee.getDeptId());
//                deptRes.setAddToOldDept(true);
//                deptRes.setRemind(
//                        String.format("由于该员工同时管理%s部门，若设定为新部门的管理员，则新部门将归入%s部门管理。"
//                                , parent.getName(), parent.getName()));
            } else {
                checkEmDataRs.setChangeEmRole(true);
            }
        } else if (employee != null && employee.getStatus() == EmStatus.FREEZE.getCode()) {
                throw new BusinessException(BizError.EM_FREEZE);
        } else {
            checkEmDataRs.setCreateEm(true);
        }
        return checkEmDataRs;
    }

    @Transactional
    public DeptRes updateDept(UpdateDeptReq req) {
        Dept dept = deptProcessService.getById(req.getDeptId());
        LoginUser user = LoginUserContext.getUser();
        DeptRes deptRes = new DeptRes();
        if (dept == null || !dept.getStatus().equals(DeptStatus.NORMAL.getCode())) {
            throw new BusinessException(BizError.DEPT_NOT_EXIST);
        }
        if (StringUtils.hasLength(req.getMobile()) && StringUtils.isEmpty(req.getManagerName())) {
            throw new BusinessException(BizError.Name_empty);
        }
        if (StringUtils.hasLength(req.getManagerName()) && StringUtils.isEmpty(req.getMobile())) {
            throw new BusinessException(BizError.Mobile_EMPTY);
        }
        if (!dept.getName().equals(req.getName())) {
            if (deptProcessService.deptExist(user.getCompanyId(), req.getName())) {
                throw new BusinessException(BizError.DEPT_EXIST);
            }
            dept.setName(req.getName());
        }
        //更新部门
        if (!dept.getpDeptId().equals(req.getPdeptId()) && req.getPdeptId() != null) {
            Dept pdept = deptProcessService.getById(req.getPdeptId());
            if (!dept.getCompanyId().equals(pdept.getCompanyId())) {
                throw new BusinessException(BizError.CANT_CHG_DEPT_CMP);
            }
            updatePdeptOfDept(dept, pdept);
        }

        //原管理员
        Employee oldManager = emService.getManager(dept);
        //变更了管理员
        if (oldManager == null && StringUtils.hasLength(req.getMobile()) || (oldManager != null && (!oldManager.getMobileNumber().equals(req.getMobile()) || !oldManager.getName().equals(req.getManagerName())))) {
            CheckEmDataRs checkEmDataRs = new CheckEmDataRs();
            if (StringUtils.hasLength(req.getMobile())) {
                checkEmDataRs = checkEmData(req, user, deptRes);
            }
            //将原有管理员换成普通员工
            if (oldManager != null && !oldManager.getMobileNumber().equals(req.getMobile())) {
                doChangeEmRole(dept, user, EmType.CM_EM, oldManager);
                //要求重新登录
                emService.logout(oldManager.getId());
            }
            // 创建员工
            if (checkEmDataRs.isCreateEm()) {
                //Employee deptManager = emService.getManager(dept);
                //emService.save(buildEm(dept, user.getName(), req.getManagerName(), req.getMobile(), EmType.CM_MANGER.getCode(),deptManager));
                //updateParentIsLeaf(deptManager);
                if (oldManager != null) {
                    validCreate();
                    emService.save(buildEm(dept, user.getName(), req.getManagerName(), req.getMobile(), EmType.CM_MANGER.getCode(),oldManager));
                    updateParentIsLeaf(oldManager);
                }
            }
            // 修改员工角色
            if (checkEmDataRs.isChangeEmRole()) {
                doChangeEmRole(dept, user, EmType.CM_MANGER, checkEmDataRs.getEmployee());
                //要求重新登录
                emService.logout(checkEmDataRs.getEmployee().getId());
            }
        }
        // 更新部门
        dept.setUpdator(user.getName());
        deptProcessService.update(dept, user.getName());
        return deptRes;
    }

    private void doChangeEmRole(Dept dept, LoginUser user, EmType type, Employee employee) {
        if (employee == null) {
            return;
        }
        employee.setDeptId(dept.getId());
        employee.setUpdateTime(new Date());
        employee.setUpdator(user.getName());
        employee.setType(type.getCode());
        employee.setDeptType(dept.getType());
        employee.setDeptCode(dept.getCode());
        emService.updateEm(employee);
    }

    @Transactional
    public void createChannel(CreateChannelReq createChannelReq) {
        LoginUser user = LoginUserContext.getUser();
        if (user.getCompanyType() != CompanyType.COMPANY.getCode()) {
            throw new BusinessException(BizError.CANT_CREATE_CHANNEL);
        }
        // 渠道、公司名称可以重复
        // 手机号不能重复（）
//        Company company = companyService.getByName(createChannelReq.getName());
//        if (company != null) {
//            throw new BusinessException(BizError.COMPANY_EXIST);
//        }
        Employee em = emService.getEmByMobile(createChannelReq.getMobile());
        if (em != null) {
            throw new BusinessException(BizError.EM_CANT_CANNEL);
        }
        boolean mobileExist = companyProcessService.checkContactMobileExist(createChannelReq.getMobile());
        if (mobileExist){
            throw new BusinessException(BizError.EM_CANT_CANNEL);
        }

        // 层级判断
        Long companyId = user.getCompanyId();
        Company emCompany = companyProcessService.getById(companyId);
        companyProcessService.verifyLevel(emCompany.getCode());

        Company data = companyProcessService.createCompany(user, createChannelReq, emCompany.getCode());
        // 审批通过后才创建部门和管理员
//        Dept dept = deptService.createDeptOfChannel(data, LoginUserContext.getUser());
//        createEm(createChannelReq.getMobile(), createChannelReq.getManagerName(), dept, EmType.MANGER_MANGER.getCode());
    }

    /**
     * app只能修改所属部门
     * @param req
     */
    @Transactional
    public void updateChannel(UpdateChannelReq req) {
        Company company = companyProcessService.getById(req.getChannelId());
        if (company == null) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        if (!company.getPDeptId().equals(req.getPDeptId()) && req.getPDeptId() != null) {
            Dept pdept = deptProcessService.getById(req.getPDeptId());
            this.updatePdeptOfChannel(company, pdept);
        }

//        company.setpDeptId(req.getPDeptId());
//        companyService.updateCompany(company);
//        // 修改管理部门
//        Dept managerDept = deptService.getManagerDept(company.getId());
//        Dept parent = deptService.getById(req.getPDeptId());
//        deptService.updateParent(managerDept, parent);


        // 当前仅支持修改所属部门
//        LoginUser user = LoginUserContext.getUser();
//        boolean updateCompany = false;
//        if (!company.getName().equals(req.getName())) {
//            Company data = companyService.getByName(req.getName());
//            if (data != null) {
//                throw new BusinessException(BizError.COMPANY_EXIST);
//            }
//            company.setName(req.getName());
//            company.setUpdateTime(new Date());
//            company.setUpdator(user.getName());
//            updateCompany = true;
//
//            // 更新管理部名称
//            Dept managerDept = deptService.getManagerDept(req.getChannelId());
//            managerDept.setName(company.getName() + "-管理部");
//            deptService.update(managerDept, user.getName());
//        }
//
//        //TODO 这里与新增逻辑一样，不能设置已存在的员工作为渠道管理员， 是否有问题？
//        Employee employee = companyService.getManager(company.getId());
//        if (!employee.getMobileNumber().equals(req.getMobile())) {
//            Employee emByMobile = emService.getEmByMobile(req.getMobile());
//            if (emByMobile != null) {
//                throw new BusinessException(BizError.EM_CANT_CANNEL);
//            }
//            Dept dept = deptService.getManagerDept(company.getId());
//            // 修改员工角色
//            doChangeEmRole(dept, user, EmType.MANGER_EM, employee);
//            //要求重新登录
//            emService.logout(employee.getId());
//            company.setContactMobile(req.getMobile());
//            company.setContact(req.getManagerName());
//            updateCompany  = true;
//            // 创建渠道管理员
////            emService.save(buildEm(dept, user.getName(), req.getName(), req.getMobile(), EmType.MANGER_MANGER.getCode()));
//            createEm(req.getMobile(), req.getManagerName(), dept, EmType.MANGER_MANGER.getCode());
//        }else {
//            if (!employee.getName().equals(req.getManagerName())){
//                throw new BusinessException(BizError.USER_EXIST);
//            }
//        }
//        if (updateCompany){
//            companyService.updateCompany(company);
//        }
    }

    private void updatePdeptOfChannel(Company company, Dept pdept){
        LoginUser loginUser = LoginUserContext.getUser();
        //修改公司
        company.setPDeptId(pdept.getId());
        companyProcessService.updateCompany(company);
        //修改管理部门
        Dept manDept = deptProcessService.getManagerDept(company.getId());

        //不能将自己设置为自己的所属部门
        if (manDept.getCode().equals(pdept.getCode())){
            throw new BusinessException(BizError.ERROR_PARENT_DEPT);
        }

        String oldManDeptCode = manDept.getCode();
        String newManDeptCode = deptProcessService.generateCode(pdept.getId(), pdept.getCode());
        manDept.setpDeptId(pdept.getId());
        manDept.setCode(newManDeptCode);
        deptProcessService.update(manDept, loginUser.getName());
        List cmpIds = new ArrayList();
        cmpIds.add(company.getId());
        //修改普通部门
        deptProcessService.updateChildsCode(newManDeptCode, oldManDeptCode, cmpIds);
        //修改员工code
        emService.updateDeptCode(cmpIds, oldManDeptCode, new Date(), loginUser.getName());
        //刷新员工redis 中token对应的数据
        emService.updateRedisEm(cmpIds, newManDeptCode);
    }

    private void updatePdeptOfDept(Dept dept, Dept pdept){
        //不能将自己设置为自己的所属部门
        if (dept.getCode().equals(pdept.getCode())){
            throw new BusinessException(BizError.ERROR_PARENT_DEPT);
        }

        LoginUser loginUser = LoginUserContext.getUser();
        String oldCode = dept.getCode();
        String newCode = deptProcessService.generateCode(pdept.getId(), pdept.getCode());
        dept.setpDeptId(pdept.getId());
        dept.setCode(newCode);
        deptProcessService.update(dept, loginUser.getName());

        //部门下的所有渠道
        List<Long> cmpIds = deptProcessService.queryCmpIdByCode(oldCode);
        cmpIds.add(dept.getCompanyId());
        //修改所有子部门
        deptProcessService.updateChildsCode(newCode, oldCode, cmpIds);
        //修改员工code
        emService.updateDeptCode(cmpIds, oldCode, new Date(), loginUser.getName());
        //刷新员工redis 中token对应的数据
        emService.updateRedisEm(cmpIds, newCode);
    }

    /**
     * 查询指定部门下的下级渠道/部门，以及本部门员工
     *
     * @param deptId
     * @return
     */
    public QueryChildRes queryDeptChildList(Long deptId) {
        LoginUser user = LoginUserContext.getUser();

        deptId = deptId == null ? user.getDeptId() : deptId;
        QueryChildRes res = new QueryChildRes();

        //设置部门信息
        Dept dept = deptProcessService.getById(deptId);
//        hasQueryDeptPerm(dept);
        if (dept == null || !dept.getStatus().equals(DeptStatus.NORMAL.getCode())) {
            throw new BusinessException(BizError.DEPT_NOT_EXIST);
        }
        res.setType(ObjectInfoType.DEPT.getCode());
        res.setId(dept.getId());
        res.setName(dept.getName());
        int total = deptProcessService.queryTotalEm(dept.getCode());
        res.setTotal(total);
        Employee manager = emService.getManager(dept);
        if (manager != null) {
            res.setManager(manager.getName());
            res.setIsManager(user.getId().equals(manager.getId()));
            res.setIsSelf(user.getId().equals(manager.getId()));
        }

        //只能编辑非管理部门的子部门
        if (dept.getCode().startsWith(user.getDeptCode() + "-") && DeptType.COMMON.getCode() == dept.getType().intValue()) {
            res.setIsUpdate(true);
        } else {
            res.setIsUpdate(false);
        }

        //设置下级渠道/部门
        List<QueryChildRes.ChildInfo> childInfos = new LinkedList<>();
        res.setChildInfos(childInfos);

        List<DeptListRs> childDepts = deptProcessService.getChildDepts(null, dept.getId());
        for (DeptListRs childDept : childDepts) {
            QueryChildRes.ChildInfo childInfo = new QueryChildRes.ChildInfo();
            //如果是管理部门，转成渠道
            if (DeptType.MANGER.getCode() == childDept.getType()) {
                Company company = companyProcessService.getById(childDept.getCompanyId());
                childInfo.setTotal(0);
//                childInfo.setTotal(deptService.queryTotalEm(childDept.getCode()));
                childInfo.setId(company.getId());
                childInfo.setName(company.getName());
                childInfo.setType(ObjectInfoType.CHANNEL.getCode());
            } else {
                childInfo.setTotal(0);
//                childInfo.setTotal(deptService.queryTotalEm(childDept.getCode()));
                childInfo.setId(childDept.getId());
                childInfo.setName(childDept.getName());
                childInfo.setType(ObjectInfoType.DEPT.getCode());
            }
            childInfos.add(childInfo);
        }

        //设置部门内员工信息
        List<Employee> employees = emService.getEmployees(dept.getId());
        for (Employee employee : employees) {
            QueryChildRes.ChildInfo childInfo = new QueryChildRes.ChildInfo();
            childInfo.setId(employee.getId());
            childInfo.setName(employee.getName());
            childInfo.setType(ObjectInfoType.EMPLOYEE.getCode());
            childInfo.setIsManager(false);
            if (manager != null && employee.getId().equals(manager.getId())) {
                childInfo.setIsManager(true);
                childInfos.add(0, childInfo);
            } else {
                childInfos.add(childInfo);
            }
            childInfo.setIsSelf(user.getId().equals(employee.getId()));
            //管理员不能被同管理部门的员工更新
            if (EmType.MANGER_MANGER.getCode() == employee.getType() && EmType.MANGER_EM.getCode() == user.getType() && employee.getDeptId().equals(user.getDeptId())) {
                childInfo.setIsUpdate(false);
            } else if (EmType.CM_EM.getCode() != user.getType() && !employee.getId().equals(user.getId())) {
                childInfo.setIsUpdate(true);
            } else {
                childInfo.setIsUpdate(false);
            }
        }
        return res;
    }

    /**
     * 查询公司下的部门（只有管理部门以及管理部门的管理员）
     *
     * @param companyId
     * @return
     */
    public QueryChildRes queryCompanyChildList(Long companyId) {
        LoginUser user = LoginUserContext.getUser();
        QueryChildRes res = new QueryChildRes();
        Company company = companyProcessService.getById(companyId);
        if (company == null || !company.getStatus().equals(CompanyStatus.NORMAL.getCode())) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        Dept managerDept = deptProcessService.getManagerDept(company.getId());
//        hasQueryCompanyPerm(company);

        Employee managerEmp = emService.getManager(managerDept);

        res.setId(company.getId());
        res.setName(company.getName());
        res.setType(ObjectInfoType.CHANNEL.getCode());
        res.setManager(managerEmp.getName());
        res.setIsManager(false);
        res.setIsSelf(false);
        res.setIsUpdate(true);

        List<QueryChildRes.ChildInfo> childInfos = new LinkedList<>();
        res.setChildInfos(childInfos);

//        公司下不再显示渠道管理员
//        QueryChildRes.ChildInfo childEmpInfo = new QueryChildRes.ChildInfo();
//        childEmpInfo.setId(managerEmp.getId());
//        childEmpInfo.setName(managerEmp.getName());
//        childEmpInfo.setType(ObjectInfoType.EMPLOYEE.getCode());
//        childEmpInfo.setIsManager(true);
//        childEmpInfo.setIsUpdate(false);
//        childInfos.add(childEmpInfo);

        QueryChildRes.ChildInfo childDeptInfo = new QueryChildRes.ChildInfo();
        childDeptInfo.setTotal(deptProcessService.queryTotalEm(managerDept.getCode()));
        childDeptInfo.setId(managerDept.getId());
        childDeptInfo.setName(managerDept.getName());
        childDeptInfo.setType(ObjectInfoType.DEPT.getCode());
        childInfos.add(childDeptInfo);

        res.setTotal(childDeptInfo.getTotal());
        return res;
    }

    @Transactional
    public void deleteEm(Long emId) {
        String accounts = dictDao.getByName("cant_delete_em");
        if (!StringUtils.isEmpty(accounts)){
            if (accounts.contains(emId + "")){
                throw new BusinessException(BizError.CANT_DELETE_EMPLOYEE_ACCOUNT);
            }
        }

        Employee em = emService.getById(emId);
        if (em == null || em.getStatus() != EmStatus.NORMAL.getCode()) {
            throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
        }
        //不能删管理部门的管理员
        if (em.getType() == EmType.MANGER_MANGER.getCode()) {
            throw new BusinessException(BizError.CHANNEL_ERROR);
        }
        LoginUser user = LoginUserContext.getUser();
        em.setUpdateTime(new Date());
        em.setUpdator(user.getName());
//        if (em.getType() == EmType.MANGER_EM.getCode()) {
//            em.setStatus(EmStatus.CANCEL.getCode());
//            emService.updateEm(em);
//            //要求重新登录
//            emService.logout(em.getId());
//            return;
//        }
        //物料和订单判断
        if (em.getType() == EmType.MANGER_EM.getCode() || em.getType() == EmType.CM_EM.getCode()) {
            em.setStatus(EmStatus.CANCEL.getCode());
            emService.updateEm(em);
            //要求重新登录
            emService.logout(em.getId());
        }
        if (em.getType() == EmType.CM_MANGER.getCode()) {
            int count = deptProcessService.queryTotalEm(em.getDeptCode());
            if (count > 1) {
                throw new BusinessException(BizError.EM_NOT_ZERO);
            }
            em.setStatus(EmStatus.CANCEL.getCode());
            emService.updateEm(em);
            //要求重新登录
            emService.logout(em.getId());
        }

        /*//删除后台管理账号
        this.deleteManAccount(em);*/
    }

    @Transactional
    public void deleteDept(Long deptId) {
        LoginUser user = LoginUserContext.getUser();
        Dept dept = deptProcessService.getById(deptId);
        if (dept == null) {
            throw new BusinessException(BizError.DEPT_NOT_EXIST);
        }
        if (dept.getType() == DeptType.MANGER.getCode()) {
            throw new BusinessException(BizError.Dept_Type_error);
        }
        int i = deptProcessService.queryTotalEm(dept.getCode());
        if (i > 0) {
            throw new BusinessException(BizError.Dept_not_empty);
        }
        //还需要判断，是否有下级部门
        List<DeptListRs> childDepts = deptProcessService.getAllChildDepts(dept.getCompanyId(), dept.getCode());
        if (CollectionUtils.isNotEmpty(childDepts)) {
            throw new BusinessException(BizError.HAS_CHILD_DEPT);
        }
        dept.setStatus(DeptStatus.CANCEL.getCode());
        deptProcessService.update(dept, user.getName());
    }

    @Transactional
    public void deleteChannel(Long companyId) {
        LoginUser user = LoginUserContext.getUser();
        Company company = companyProcessService.getById(companyId);
        if (company == null) {
            throw new BusinessException(BizError.COMPANY_NOT_EXIST);
        }
        Dept dept = deptProcessService.getManagerDept(companyId);
        emService.deleteChannel(dept.getCode(), user);
        deptProcessService.deleteChannel(dept.getCode(), user);
        companyProcessService.deleteChannel(company.getCode(), user.getName());
        //?下线所有渠道的员工
        emService.logoutEmpsByDeptCode(dept.getCode());
    }

    public List<EmInfo> queryByNameOrMobile(String express) {
        LoginUser user = LoginUserContext.getUser();
        boolean hadManger = false;
        if (user.getType() == EmType.CM_EM.getCode()) {
            hadManger = emService.getManager(user.getDeptId(), EmType.CM_MANGER.getCode()) != null;
        }

        Dept dept = deptProcessService.getParentDept(user.getDeptId());
        if (dept != null && dept.getType() == DeptType.COMMON.getCode()) {
            if (emService.getManager(dept) == null) {
                dept = deptProcessService.getParentDept(dept.getId());
            }
        }

        List<EmInfo> list = emService.queryByNameOrMobile(express, user, dept, hadManger);
        return list.stream().filter(item->item.getId()!=1L).collect(Collectors.toList());
    }

//    /**
//     * 是否有操作该部门人员的权限(本部门或者下级部门)
//     *
//     * @param dept
//     * @return
//     */
//    private void hasOpsDeptEmPerm(Dept dept) {
//        LoginUser user = LoginUserContext.getUser();
//        if (dept == null || !dept.getStatus().equals(DeptStatus.NORMAL.getCode())) {
//            throw new BusinessException(BizError.DEPT_NOT_EXIST);
//        }
//        boolean result = true;
//        //不是一个公司
//        if (!dept.getCompanyId().equals(user.getCompanyId())) {
//            result = false;
//        }
//        //本部门只有管理员可以操作
//        if (dept.getCode().equals(user.getDeptCode()) && user.getType() != EmType.CM_MANGER.getCode() && user.getType() != EmType.MANGER_MANGER.getCode()) {
//            result = false;
//        }
//        //下级部门, 管理员，管理部门员工，都可以操作
//        if (dept.getCode().startsWith(user.getDeptCode() + "-") && user.getType() == EmType.CM_EM.getCode()){
//            result = false;
//        }
//        //是否是自己的部门或者子部门
//        if (!dept.getCode().startsWith(user.getDeptCode() + "-") && !dept.getCode().equals(user.getDeptCode())) {
//            result = false;
//        }
//        if (!result){
//            throw new BusinessException(BizError.EMP_POWER_ERROR);
//        }
//    }
//
//    /**
//     * 是否有操作该部门的权限(部门一定是下级部门)
//     *
//     * @param dept
//     * @return
//     */
//    private void hasOpsDeptPerm(Dept dept) {
//        LoginUser user = LoginUserContext.getUser();
//        if (dept == null || !dept.getStatus().equals(DeptStatus.NORMAL.getCode())) {
//            throw new BusinessException(BizError.DEPT_NOT_EXIST);
//        }
//        boolean result = true;
//        //不是一个公司
//        if (!dept.getCompanyId().equals(user.getCompanyId())) {
//            result = false;
//        }
//        //下级部门, 管理员，管理部门员工，都可以操作
//        if (dept.getCode().startsWith(user.getDeptCode() + "-") && user.getType() == EmType.CM_EM.getCode()){
//            result = false;
//        }
//        //只能操作下级部门
//        if (!dept.getCode().startsWith(user.getDeptCode() + "-")) {
//            result = false;
//        }
//        if (!result){
//            throw new BusinessException(BizError.EMP_POWER_ERROR);
//        }
//    }

//    /**
//     * 是否有查询该部门的员工及下面的渠道，部门的权限
//     *
//     * @param dept
//     * @return
//     */
//    private void hasQueryDeptPerm(Dept dept) {
//        LoginUser user = LoginUserContext.getUser();
//        if (dept == null || !dept.getStatus().equals(DeptStatus.NORMAL.getCode())) {
//            throw new BusinessException(BizError.DEPT_NOT_EXIST);
//        }
//        boolean result = true;
//        //只有管理员可以查询
//        if (user.getType() == EmType.CM_EM.getCode()) {
//            result = false;
//        }
//        //是否是自己的部门或者子部门
//        if (!dept.getCode().startsWith(user.getDeptCode() + "-") && !dept.getCode().equals(user.getDeptCode())) {
//            result = false;
//        }
//        if (!result){
//            throw new BusinessException(BizError.EMP_POWER_ERROR);
//        }
//    }
//
//    /**
//     * 是否有查询该公司的权限
//     *
//     * @param company
//     * @return
//     */
//    private void hasQueryCompanyPerm(Company company) {
//        LoginUser user = LoginUserContext.getUser();
//        if (company == null || !company.getStatus().equals(CompanyStatus.NORMAL.getCode())) {
//            throw new BusinessException(BizError.DEPT_NOT_EXIST);
//        }
//        boolean result = true;
//        //只有管理员可以查询
//        if (user.getType() == EmType.CM_EM.getCode()) {
//            result = false;
//        }
//        Dept managerDept = deptService.getManagerDept(company.getId());
//        //只能看下级的渠道信息
//        if (!managerDept.getCode().startsWith(LoginUserContext.getUser().getDeptCode() + "-")) {
//            result = false;
//        }
//        if (!result){
//            throw new BusinessException(BizError.EMP_POWER_ERROR);
//        }
//    }


    /**
     * 新增后台管理的用户-只针对公司的管理部门人员开通账号
     */
   /* private void createManAccount(Employee emp) {
        if (emp.getCompanyType().equals(CompanyType.COMPANY.getCode()) && emp.getDeptType().equals(DeptType.MANGER.getCode())) {
            sysUserService.insert(emp.getMobileNumber(), emp.getName(), emp.getId());
        }
    }*/

    /**
     * 更新后台账号，
     * 如果更新了部门，新部门是安逸出行管理部，则新增。 新部门不是安逸出行管理部，则删除后台账号（如果老部门不是安逸出行管理部则没有账号可删）
     * 没有更新部门，直接更新后台账号。
     *
     * @param emp
     */
   /* private void updateManAccount(Employee emp, boolean modifyDept) {
        if (modifyDept) {
            if (emp.getCompanyType().equals(CompanyType.COMPANY.getCode()) && emp.getDeptType().equals(DeptType.MANGER.getCode())) {
                sysUserService.insert(emp.getMobileNumber(), emp.getName(), emp.getId());
            } else {
                sysUserService.delByEmployeeId(emp.getId());
            }
        } else {
            sysUserService.updateByEmployeeId(emp.getMobileNumber(), emp.getName(), emp.getId());
        }
    }

    private void deleteManAccount(Employee emp) {
        if (emp.getCompanyType().equals(CompanyType.COMPANY.getCode()) && emp.getDeptType().equals(DeptType.MANGER.getCode())) {
            sysUserService.delByEmployeeId(emp.getId());
        }
    }*/
}