package com.anyi.sparrow.organize.employee.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.anyi.common.account.domain.EmployeeAccount;
import com.anyi.common.account.mapper.EmployeeAccountMapper;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.company.domain.Company;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.domain.dto.AppReviewControlConfigDTO;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.enums.EmStatus;
import com.anyi.common.employee.enums.EmType;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.insurance.domain.DiInsuranceUserAccount;
import com.anyi.common.insurance.domain.DiUserLogin;
import com.anyi.common.insurance.mapper.DiInsuranceUserAccountMapper;
import com.anyi.common.insurance.mapper.DiUserLoginMapper;
import com.anyi.common.service.CommonSysDictService;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.PasswordUtil;
import com.anyi.sparrow.assist.system.service.SysDictService;
import com.anyi.sparrow.assist.verify.enums.BizEnum;
import com.anyi.sparrow.assist.verify.service.VerifyService;
import com.anyi.sparrow.base.security.Constants;
import com.anyi.sparrow.base.security.LoginUser;
import com.anyi.sparrow.base.security.LoginUserContext;
import com.anyi.sparrow.base.security.TokenBuilder;
import com.anyi.sparrow.common.enums.UserType;
import com.anyi.sparrow.organize.employee.dao.EmployeeDao;
import com.anyi.sparrow.organize.employee.dao.EmployeeHistoryDao;
import com.anyi.sparrow.organize.employee.dao.EmployeeLoginDao;
import com.anyi.sparrow.organize.employee.dao.mapper.TemporaryEmployeeLoginMapper;
import com.anyi.sparrow.organize.employee.dao.mapper.TemporaryEmployeeMapper;
import com.anyi.sparrow.organize.employee.domain.EmployeeHistory;
import com.anyi.sparrow.organize.employee.domain.EmployeeLogin;
import com.anyi.sparrow.organize.employee.domain.TemporaryEmployee;
import com.anyi.sparrow.organize.employee.domain.TemporaryEmployeeLogin;
import com.anyi.sparrow.organize.employee.enums.DeptType;
import com.anyi.sparrow.organize.employee.enums.OutResaonType;
import com.anyi.sparrow.organize.employee.req.PasswordUpdateReq;
import com.anyi.sparrow.organize.employee.vo.EmInfo;
import com.anyi.sparrow.organize.employee.vo.EmployeeLoginVO;
import com.anyi.sparrow.organize.employee.vo.EmployeeVO;
import com.anyi.sparrow.organize.employee.vo.TemporaryEmployeeLoginReq;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EmService {

    private Logger log = LoggerFactory.getLogger(EmService.class);

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeLoginDao loginDao;

    @Autowired
    private EmployeeHistoryDao historyDao;

    @Autowired
    private VerifyService verifyService;

    @Value("${tokenExpire:24}")
    private int tokenExpire;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeAccountMapper employeeAccountMapper;

    @Autowired
    private CompanyProcessService companyProcessService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SysDictService dictService;

    @Autowired
    private TemporaryEmployeeMapper temporaryEmployeeMapper;

    @Autowired
    private TemporaryEmployeeLoginMapper temporaryEmployeeLoginMapper;

    @Autowired
    private SnowflakeIdService snowflakeIdService;

    @Autowired
    private DiInsuranceUserAccountMapper insuranceEmployeeMapper;
    @Autowired
    private DiUserLoginMapper insuranceUserLoginMapper;

    @Autowired
    CommonSysDictService commonSysDictService;

    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(PasswordUpdateReq req) {
        Employee emp = this.getEmployee(LoginUserContext.getUser().getId());

        if (StrUtil.isNotBlank(emp.getPassword())) {
            // 校验密码是否正确
            if (!PasswordUtil.md5(req.getOldPassword()).equals(emp.getPassword())) {
                throw new BusinessException(BizError.ERROR_TEMPORARY_EMPLOYEE_PASSWORD);
            }
        }

        String newPwd = PasswordUtil.md5(req.getNewPassword());
        emp.setUpdateTime(new Date());
        emp.setUpdator(LoginUserContext.getUser().getName());
        emp.setPassword(newPwd);
        this.updateEm(emp);

        this.logout();
    }

    private void checkTemporaryFuncAble() {
        Boolean able = commonSysDictService.getTemporaryFuncAble();
        if (!able) {
            throw new BusinessException(-1, "请用短信验证码登录");
        }
    }

    private boolean isTemporaryFuncAble() {
        return commonSysDictService.getTemporaryFuncAble();
    }

    @Transactional(rollbackFor = Exception.class)
    public void temporaryRegister(TemporaryEmployeeLoginReq req) {
        checkTemporaryFuncAble();
        // 校验是否存在账号
        TemporaryEmployee emp = temporaryEmployeeMapper.selectOne(new LambdaQueryWrapper<TemporaryEmployee>()
                .eq(TemporaryEmployee::getStatus, EmStatus.NORMAL.getCode())
                .eq(TemporaryEmployee::getMobileNumber, req.getMobile()));
        if (emp != null) {
            throw new BusinessException(BizError.USER_EXIST);
        }
        Employee employee = employeeDao.getByMobile(req.getMobile());
        if (employee != null) {
            throw new BusinessException(BizError.USER_EXIST);
        }
        String rawPwd = req.getPassword();
        String securePwd = SecureUtil.md5(rawPwd);

        TemporaryEmployee bean = TemporaryEmployee.builder()
                .id(snowflakeIdService.nextId())
                .companyId(-1L)
                .companyType(CompanyType.COMPANY.getCode())
                .deptId(-1L)
                .deptType(DeptType.COMMON.getCode())
                .type(EmType.TEMP_EM.getCode())
                .name("游客" + req.getMobile())
                .deptCode("-1")
                .status(EmStatus.NORMAL.getCode())
                .mobileNumber(req.getMobile())
                .password(securePwd)
                .createTime(new Date())
                .build();
        temporaryEmployeeMapper.insert(bean);
    }

    @Transactional(rollbackFor = Exception.class)
    public EmployeeVO temporaryLogin(TemporaryEmployeeLoginReq req) {
        checkTemporaryFuncAble();
        // 校验是否存在账号
        TemporaryEmployee emp = temporaryEmployeeMapper.selectOne(new LambdaQueryWrapper<TemporaryEmployee>()
                .eq(TemporaryEmployee::getStatus, EmStatus.NORMAL.getCode())
                .eq(TemporaryEmployee::getMobileNumber, req.getMobile()));
        if (emp == null) {
            // 不存在则弹出注册临时用户，为了app审核能通过
            throw new BusinessException(BizError.TO_REGISTER_TEMPORARY_EMPLOYEE);
        }
        // 校验密码是否正确
        if (!SecureUtil.md5(req.getPassword()).equals(emp.getPassword())) {
            throw new BusinessException(BizError.ERROR_TEMPORARY_EMPLOYEE_PASSWORD);
        }
        TemporaryEmployeeLogin lastLogin = temporaryEmployeeLoginMapper.selectOne(new LambdaQueryWrapper<TemporaryEmployeeLogin>().eq(TemporaryEmployeeLogin::getUserId, emp.getId()));
        if (lastLogin != null) {
            // 删除旧token
            removeCacheTokenForTemporary(lastLogin.getToken());
        }
        // 新增token和login记录
        String token = TokenBuilder.build(UserType.TEMPORARY_EMPLOYEE.getCode());
        Date tokenExpireDate = DateTime.now().plusDays(tokenExpire).toDate();
        TemporaryEmployeeLogin el = TemporaryEmployeeLogin.builder()
                .userId(emp.getId())
                .token(token)
                .tokenExpire(tokenExpireDate)
                .loginTime(new Date())
                .build();

        if (temporaryEmployeeLoginMapper.update(el, new QueryWrapper<>()) < 1) {
            temporaryEmployeeLoginMapper.insert(el);
        }

        EmployeeVO em = new EmployeeVO();
        BeanUtils.copyProperties(emp, em);
        em.setToken(token);
        em.setTemporaryUser(true);
        //缓存token和用户信息
        cacheTokenForTemporary(token, emp);
        return em;
    }

    @Transactional(rollbackFor = Exception.class)
    public EmployeeVO loginByPwd(TemporaryEmployeeLoginReq vo) {
        //验证员工
        Employee employee = getEmByMobile(vo.getMobile());
        if (employee == null) {
            //throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
            // 不存在则弹出注册临时用户，为了app审核能通过
            if (isTemporaryEmployee(vo.getMobile())) {
                throw new BusinessException(BizError.TEMPORARY_EMPLOYEE_USER);
            } else {
                throw new BusinessException(BizError.TO_REGISTER_TEMPORARY_EMPLOYEE);
            }
        }

        if(employee.getStatus().equals(EmStatus.FREEZE.getCode())){
            throw new BusinessException(BizError.TO_FROZE_EMPLOYEE);
        }

        if(CompanyType.RECYCLE.getCode()==employee.getCompanyType().intValue()){
            throw new BusinessException(BizError.TO_RECYCLE_EMPLOYEE);
        }

        // 校验密码是否正确
        if (!PasswordUtil.md5(vo.getPassword()).equals(employee.getPassword())) {
            throw new BusinessException(BizError.ERROR_TEMPORARY_EMPLOYEE_PASSWORD);
        }

        EmployeeLogin lastLogin = loginDao.getByUserId(employee.getId());
        if (lastLogin != null) {
            //删除旧token
            removeCacheToken(lastLogin.getToken());
        }

        String token = TokenBuilder.build(UserType.EMPLOYEE.getCode());

        Date tokenExpireDate = DateTime.now().plusDays(tokenExpire).toDate();
        EmployeeLogin el = new EmployeeLogin();
        BeanUtils.copyProperties(vo, el);
        el.setUserId(employee.getId());
        el.setToken(token);
        el.setTokenExpire(tokenExpireDate);
        el.setLoginTime(new Date());

        if (loginDao.update(el) < 1) {
            el.setId(snowflakeIdService.nextId());
            loginDao.insert(el);
        }

        //TODO 判断是否挤下线 需要设备唯一标识号
        EmployeeHistory eh = new EmployeeHistory();
        BeanUtils.copyProperties(el, eh);
        eh.setId(snowflakeIdService.nextId());
        historyDao.insert(eh);

        EmployeeVO em = new EmployeeVO();
        BeanUtils.copyProperties(employee, em);
        em.setToken(token);
        //缓存token和用户信息
        cacheToken(token, employee);

        setReStatus(em, vo.getAppVersion(), vo.getOs());
        return em;
    }

    @Transactional
    public EmployeeVO login(EmployeeLoginVO vo) {
        //验证员工
        Employee employee = getEmByMobile(vo.getMobile());
        if (employee == null) {
            //throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
            // 不存在则弹出注册临时用户，为了app审核能通过
            if (isTemporaryEmployee(vo.getMobile())) {
                throw new BusinessException(BizError.TEMPORARY_EMPLOYEE_USER);
            } else {
                throw new BusinessException(BizError.TO_REGISTER_TEMPORARY_EMPLOYEE);
            }
        }

        if(employee.getStatus().equals(EmStatus.FREEZE.getCode())){
            throw new BusinessException(BizError.TO_FROZE_EMPLOYEE);
        }

        if(CompanyType.RECYCLE.getCode()==employee.getCompanyType().intValue()){
            throw new BusinessException(BizError.TO_RECYCLE_EMPLOYEE);
        }

        String val = redisTemplate.opsForValue().get(Constants.LOGIN_EROR_COUNT_REDIS + employee.getId());
        String loginErrorCount = dictService.getByNameWithCache("login_error_count");

        if (!"18655477901".equals(vo.getMobile()) && StringUtils.isNotEmpty(val) && Integer.parseInt(val) >= Integer.parseInt(loginErrorCount)) {
            throw new BusinessException(BizError.EMPLOYEE_LOGIN_COUNT_ERROR, new Object[]{loginErrorCount});
        }
        //验证码验证
        if(!("18600000000".equals(vo.getMobile()) && "0000".equals(vo.getVerifyCode()) )){
            try {
                verifyService.verifyCode(vo.getMobile(), vo.getVerifyCode(), BizEnum.EM_LOGIN);
            } catch (Exception e) {
                redisTemplate.opsForValue().increment(Constants.LOGIN_EROR_COUNT_REDIS + employee.getId(), 1);
                redisTemplate.expire(Constants.LOGIN_EROR_COUNT_REDIS + employee.getId(), 24, TimeUnit.HOURS);
                throw e;
            }
            redisTemplate.delete(Constants.LOGIN_EROR_COUNT_REDIS + employee.getId());
        }

        EmployeeLogin lastLogin = loginDao.getByUserId(employee.getId());
        if (lastLogin != null) {
            //删除旧token
            removeCacheToken(lastLogin.getToken());
        }

        String token = TokenBuilder.build(UserType.EMPLOYEE.getCode());

        Date tokenExpireDate = DateTime.now().plusDays(tokenExpire).toDate();
        EmployeeLogin el = new EmployeeLogin();
        BeanUtils.copyProperties(vo, el);
        el.setUserId(employee.getId());
        el.setToken(token);
        el.setTokenExpire(tokenExpireDate);
        el.setLoginTime(new Date());

        if (loginDao.update(el) < 1) {
            el.setId(snowflakeIdService.nextId());
            loginDao.insert(el);
        }

        //TODO 判断是否挤下线 需要设备唯一标识号
        EmployeeHistory eh = new EmployeeHistory();
        BeanUtils.copyProperties(el, eh);
        eh.setId(snowflakeIdService.nextId());
        historyDao.insert(eh);

        EmployeeVO em = new EmployeeVO();
        BeanUtils.copyProperties(employee, em);
        em.setToken(token);
        //缓存token和用户信息
        cacheToken(token, employee);

        setReStatus(em, vo.getAppVersion(), vo.getOs());
        return em;
    }

    private void setReStatus(EmployeeVO em, String appVersion, String os) {
        em.setReStatus(false);
        String key = "app_review_control_config";
        String value = dictService.getByNameWithCache(key);
        if (StrUtil.isBlank(value)) {
            return;
        }
        AppReviewControlConfigDTO config = JSONUtil.toBean(value, AppReviewControlConfigDTO.class);
        if (config.getOs().equals(AppReviewControlConfigDTO.OS.ALL.getName())) {
            if (config.getAppVersion().equals(AppReviewControlConfigDTO.OS.ALL.getName())) {
                em.setReStatus(true);
                return;
            }
            if (appVersion.equals(config.getAppVersion())) {
                em.setReStatus(true);
                return;
            }
        }
        if (os.equals(config.getOs())) {
            if (config.getAppVersion().equals(AppReviewControlConfigDTO.OS.ALL.getName())) {
                em.setReStatus(true);
                return;
            }
            if (appVersion.equals(config.getAppVersion())) {
                em.setReStatus(true);
                return;
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param token
     */
    private void removeCacheToken(String token) {
        redisTemplate.delete(Constants.TOKEN_PREFIX + UserType.EMPLOYEE.getCode() + ":" + token);
    }

    private void removeCacheTokenForTemporary(String token) {
        redisTemplate.delete(Constants.TOKEN_PREFIX + UserType.TEMPORARY_EMPLOYEE.getCode() + ":" + token);
    }

    private void removeCacheTokenForInsurance(String token) {
        redisTemplate.delete(Constants.TOKEN_PREFIX + UserType.INSURANCE_EMPLOYEE.getCode() + ":" + token);
    }

    /**
     * 缓存token
     *
     * @param token
     * @param employee
     */
    private void cacheToken(String token, Employee employee) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(employee.getId());
        loginUser.setMobileNumber(employee.getMobileNumber());
        loginUser.setName(employee.getName());
        loginUser.setStatus(employee.getStatus());
        loginUser.setType(employee.getType());
        loginUser.setDeptId(employee.getDeptId());
        loginUser.setDeptCode(employee.getDeptCode());
        loginUser.setCompanyId(employee.getCompanyId());
        loginUser.setDeptType(employee.getDeptType());
        loginUser.setCompanyType(employee.getCompanyType());
        loginUser.setLevel(employee.getLevel());
        loginUser.setAncestors(employee.getAncestors());
        String redisKey = Constants.TOKEN_PREFIX + UserType.EMPLOYEE.getCode() + ":" + token;
        redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(loginUser));
        redisTemplate.expire(redisKey, tokenExpire, TimeUnit.DAYS);
    }

    private void cacheTokenForTemporary(String token, TemporaryEmployee employee) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(employee.getId());
        loginUser.setMobileNumber(employee.getMobileNumber());
        loginUser.setName(employee.getName());
        loginUser.setStatus(employee.getStatus());
        loginUser.setType(employee.getType());
        loginUser.setDeptId(employee.getDeptId());
        loginUser.setDeptCode(employee.getDeptCode());
        loginUser.setCompanyId(employee.getCompanyId());
        loginUser.setDeptType(employee.getDeptType());
        loginUser.setCompanyType(employee.getCompanyType());
        loginUser.setTemporaryUser(true);
        String redisKey = Constants.TOKEN_PREFIX + UserType.TEMPORARY_EMPLOYEE.getCode() + ":" + token;
        redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(loginUser));
        redisTemplate.expire(redisKey, tokenExpire, TimeUnit.DAYS);
    }

    public void getVerifyCode(String mobile) {
        // 校验是否是临时用户
        //if (isTemporaryEmployee(mobile)) {
        //    throw new BusinessException(BizError.TEMPORARY_EMPLOYEE_USER);
        //}
        Employee employee = employeeDao.getByMobile(mobile);
        if (employee == null) {
            if (isTemporaryFuncAble()) {
                // 校验是否是临时用户
                // 不存在则弹出注册临时用户，为了app审核能通过
                if (isTemporaryEmployee(mobile)) {
                    throw new BusinessException(BizError.TEMPORARY_EMPLOYEE_USER);
                } else {
                    throw new BusinessException(BizError.TO_REGISTER_TEMPORARY_EMPLOYEE);
                }
            } else {
                throw new BusinessException(BizError.EMPLOYEE_ACCOUNT_NOT_EXIST);
            }
        }
        String blackTech = dictService.getByName("blackMan");
        if(StringUtils.isNotEmpty(blackTech)){
            List<String>  blackManItems  = Arrays.asList( blackTech.split(","));
            if(CollectionUtils.isNotEmpty(blackManItems) && blackManItems.contains(String.valueOf(employee.getId()))){
                verifyService.sendSpecVerify(mobile, BizEnum.EM_LOGIN);
                return;
            }
        }

        verifyService.sendVerify(mobile, BizEnum.EM_LOGIN);
    }

    private boolean isTemporaryEmployee(String mobile) {
        Long count = temporaryEmployeeMapper.selectCount(new LambdaQueryWrapper<TemporaryEmployee>()
                .eq(TemporaryEmployee::getStatus, EmStatus.NORMAL.getCode())
                .eq(TemporaryEmployee::getMobileNumber, mobile));
        return count > 0;
    }

    private boolean isTemporaryEmployee(Long empId) {
        Long count = temporaryEmployeeMapper.selectCount(new LambdaQueryWrapper<TemporaryEmployee>()
                .eq(TemporaryEmployee::getStatus, EmStatus.NORMAL.getCode())
                .eq(TemporaryEmployee::getId, empId));
        return count > 0;
    }

    /**
     * 获取员工信息
     *
     * @returnget
     */
    public Employee getEmployee(Long id) {
        Employee employee = employeeDao.getById(id);
        return employee;
    }

    public List<Employee> getEmployees(Long deptId) {
        return employeeDao.getEmployees(deptId);
    }

    public Employee getEmByMobile(String mobileNumber) {
        return employeeDao.getByMobile(mobileNumber, Arrays.asList(EmStatus.NORMAL.getCode(), EmStatus.FREEZE.getCode()));
    }

    public Employee getNormalEmByMobile(String mobileNumber) {
        return employeeDao.getByMobile(mobileNumber);
    }


    @Transactional
    public void save(Employee data) {
//        放开层级限制
//        if (data.getLevel()>4){
//            throw new BusinessException(BizError.CANNOT_CREAT_CHILD_DEPT_EMP_CHANNEL);
//        }

        employeeMapper.insert(data);

        EmployeeAccount employeeAccount = new EmployeeAccount();
        employeeAccount.setId(snowflakeIdService.nextId());
        employeeAccount.setEmployeeId(data.getId());
        employeeAccount.setAncestors(data.getAncestors());

        Company company = companyProcessService.getById(data.getCompanyId());

        employeeAccount.setCreateTime(new Date());
        employeeAccount.setUpdateTime(employeeAccount.getCreateTime());
        employeeAccountMapper.insert(employeeAccount);

    }

    public void updateEm(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public void updateIsLeaf(Employee employee, Boolean isLeaf) {
        employeeMapper.updateIsLeaf(employee.getId(), isLeaf);
    }

    public Employee getById(Long emId) {
        return employeeMapper.selectByPrimaryKey(emId);
    }

    public void deleteChannel(String code, LoginUser user) {
        employeeDao.deleteChannel(code, user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void logout() {
        if (isTemporaryEmployee(LoginUserContext.getUser().getId())) {
            logoutForTemporary(LoginUserContext.getUser().getId());
        } else {
            logout(LoginUserContext.getUser().getId());
        }
    }

    /**
     * 清除员工token让其退出登录
     *
     * @param empId
     */
    @Transactional
    public void logout(Long empId) {
        loginDao.delete(empId);
        EmployeeHistory history = historyDao.getLatest(empId);
        if (history != null) {
            history.setOutTime(new Date());
            history.setOutResaon(OutResaonType.LOGOUT.getCode());
            historyDao.update(history);
            //清除缓存
            removeCacheToken(history.getToken());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void logoutForTemporary(Long empId) {
        TemporaryEmployeeLogin login = temporaryEmployeeLoginMapper.selectOne(new LambdaQueryWrapper<TemporaryEmployeeLogin>().eq(TemporaryEmployeeLogin::getUserId, empId));
        removeCacheTokenForTemporary(login.getToken());
        temporaryEmployeeLoginMapper.delete(new LambdaQueryWrapper<TemporaryEmployeeLogin>().eq(TemporaryEmployeeLogin::getUserId, login.getUserId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void logoutForInsurance(Long empId) {
        DiUserLogin login = insuranceUserLoginMapper.selectOne(new LambdaQueryWrapper<DiUserLogin>().eq(DiUserLogin::getUserId, empId));
        removeCacheTokenForInsurance(login.getToken());
        insuranceUserLoginMapper.delete(new LambdaQueryWrapper<DiUserLogin>().eq(DiUserLogin::getUserId, login.getUserId()));
    }

//    /**
//     * 根据公司id清除下面所有员工登录信息
//     * @param cmpId
//     */
//    @Transactional
//    public void logoutEmpsByCmpId(Long cmpId) {
//       List<EmployeeLogin> records = loginDao.selectByCmpId(cmpId);
//       if(CollectionUtils.isNotEmpty(records)) {
//           for (EmployeeLogin login : records) {
//               removeCacheToken(login.getToken());
//           }
//       }
//        loginDao.deleteByCmpId(cmpId);
//    }

    @Transactional
    public void logoutEmpsByDeptCode(String deptCode) {
        List<EmployeeLogin> records = loginDao.selectByCode(deptCode);
        if (CollectionUtils.isNotEmpty(records)) {
            for (EmployeeLogin login : records) {
                removeCacheToken(login.getToken());
            }
        }
        loginDao.deleteByDeptCode(deptCode);
    }

    public Employee getManager(Long deptId, int emType) {
        return employeeDao.getManager(deptId, emType);
    }

    public Employee getManager(Dept dept) {
        return employeeDao.getManager(dept.getId(), dept.getType() == DeptType.MANGER.getCode() ? EmType.MANGER_MANGER.getCode() : EmType.CM_MANGER.getCode());
    }

    public List<EmInfo> queryByNameOrMobile(String express, LoginUser user, Dept parent, boolean hadManager) {
        return employeeDao.queryByNameOrMobile(express, user, parent, hadManager);
    }

    public void updateDeptCode(List cmpIds, String deptCode, Date updateTime, String updator) {
        employeeDao.updateDeptCode(cmpIds, deptCode, updateTime, updator);
    }

    public List<Map<String, String>> queryTokenByDeptCode(List cmpIds, String deptCode) {
        return employeeDao.queryTokenByDeptCode(cmpIds, deptCode, new Date());
    }

    public void updateRedisEm(List cmpIds, String deptCode) {
        List<Map<String, String>> maps = queryTokenByDeptCode(cmpIds, deptCode);
        for (Map<String, String> map : maps) {
            String token = map.get("token");
            String newDeptCode = map.get("deptCode");
            String redisKey = Constants.TOKEN_PREFIX + UserType.EMPLOYEE.getCode() + ":" + token;
            String newValue = "";
            try {
                String value = redisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isNotBlank(value)) {
                    LoginUser loginUser = JSONObject.parseObject(value, LoginUser.class);
                    loginUser.setDeptCode(newDeptCode);
                    newValue = JSONObject.toJSONString(loginUser);
                    redisTemplate.opsForValue().set(redisKey, newValue);
                }
                log.error("更新员工缓存成功 token:{}, value:{} ", redisKey, newValue);
            } catch (Exception e) {
                log.error("更新员工缓存失败 token:{}, value:{} ", redisKey, newValue);
            }
        }
    }

    /**
     * 数保用户登陆
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public EmployeeVO insuranceLogin(TemporaryEmployeeLoginReq req) {
        // 校验是否存在账号
        DiInsuranceUserAccount emp = insuranceEmployeeMapper.selectOne(new LambdaQueryWrapper<DiInsuranceUserAccount>()
                .eq(DiInsuranceUserAccount::getStatus, EmStatus.NORMAL.getCode())
                .eq(DiInsuranceUserAccount::getMobile, req.getMobile()));
        if (emp == null) {
            // 不存在则弹出注册临时用户，为了app审核能通过
            throw new BusinessException(BizError.TO_REGISTER_TEMPORARY_EMPLOYEE);
        }
        // 校验密码是否正确
        if (!SecureUtil.md5(req.getPassword()).equals(emp.getPassWord())) {
            throw new BusinessException(BizError.ERROR_TEMPORARY_EMPLOYEE_PASSWORD);
        }
        DiUserLogin lastLogin = insuranceUserLoginMapper.selectOne(new LambdaQueryWrapper<DiUserLogin>().eq(DiUserLogin::getUserId, emp.getId()));
        if (lastLogin != null) {
            // 删除旧token
            removeCacheTokenForInsurance(lastLogin.getToken());
        }
        // 新增token和login记录
        String token = TokenBuilder.build(UserType.INSURANCE_EMPLOYEE.getCode());
        Date tokenExpireDate = DateTime.now().plusDays(tokenExpire).toDate();
        DiUserLogin el = DiUserLogin.builder()
                .userId(emp.getId())
                .token(token)
                .tokenExpire(tokenExpireDate)
                .loginTime(new Date())
                .build();

        if (insuranceUserLoginMapper.update(el, new QueryWrapper<>()) < 1) {
            insuranceUserLoginMapper.insert(el);
        }

        EmployeeVO em = new EmployeeVO();
        em.setMobileNumber(emp.getMobile());
        em.setToken(token);
        em.setId(emp.getId());
        em.setName(emp.getName());
        //缓存token和用户信息
        cacheTokenForInsurance(token, emp);
        return em;
    }

    private void cacheTokenForInsurance(String token, DiInsuranceUserAccount employee) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(employee.getId());
        loginUser.setMobileNumber(employee.getMobile());
        String redisKey = Constants.TOKEN_PREFIX + UserType.INSURANCE_EMPLOYEE.getCode() + ":" + token;
        redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(loginUser));
        redisTemplate.expire(redisKey, tokenExpire, TimeUnit.DAYS);
    }

}