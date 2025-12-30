package com.anyi.common.commission.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.account.constant.EmployAccountChangeEnum;
import com.anyi.common.account.service.EmployeeAccountChangeService;
import com.anyi.common.commission.domain.*;
import com.anyi.common.commission.dto.CommissionDTO;
import com.anyi.common.commission.dto.PlanConfDTO;
import com.anyi.common.commission.dto.RuleVersionDTO;
import com.anyi.common.commission.dto.income.PersonStatDTO;
import com.anyi.common.commission.dto.income.TeamWaitSettlesDetailDTO;
import com.anyi.common.commission.dto.income.WaitSettlesDTO;
import com.anyi.common.commission.enums.CommissionBizType;
import com.anyi.common.commission.enums.CommissionPackage;
import com.anyi.common.commission.enums.CommissionSettleGainType;
import com.anyi.common.commission.enums.SettleStatus;
import com.anyi.common.commission.mapper.CommissionSettleMapper;
import com.anyi.common.company.enums.CompanyType;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.mapper.EmployeeMapper;
import com.anyi.common.req.PageReq;
import com.anyi.common.snowWork.SnowflakeIdService;
import com.anyi.common.util.MoneyUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
@Slf4j
public class CommissionSettleService extends ServiceImpl<CommissionSettleMapper, CommissionSettle> {
    @Autowired
    private CommissionPlanMembersService commissionPlanMemberService;
    @Autowired
    private CommissionPlanConfService commissionPlanConfService;
    @Resource
    private EmployeeMapper employeeMapper;
    @Autowired
    private OrderCommissionRuleService orderCommissionRuleService;
    @Autowired
    private SnowflakeIdService snowflakeIdService;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private CommissionSettleCheckService  commissionSettleCheckService;


    /**
     * 固定值分佣规则绑定
     *
     * @param orderId
     * @param employeeId
     * @see CommissionBizType 方案ID
     * @see CommissionPackage 套餐ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderBindSettleRule(Long orderId, CommissionBizType typeEnum, Long commissionPackageId, Long employeeId) {
        Boolean existsFlag = orderCommissionRuleService.checkOrderRuleExists(orderId,typeEnum,commissionPackageId);
        if (existsFlag) {
            log.info("订单{}的佣金结算规则已锁定", orderId);
            return;
        }
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = getSuperEmployees(ancestorsCode);
        List<RuleVersionDTO> ruleVersions = convertToRuleVersion(superEmployees);
        // 转化为待入库的规则描述存储对象
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        if (ruleVersions.size() > 1) {
            for (int i = 1; i < superEmployees.size(); i++) {
                Employee superEmployee = superEmployees.get(i);
                // 根据成员id获取被分配的方案
                CommissionPlanMembers commissionPlanMember = commissionPlanMemberService.getCommissionPlanMemberByMemberId(superEmployee.getId(), typeEnum.getType().longValue());
                RuleVersionDTO ruleVersionDTO = ruleVersions.get(i - 1);
                if (commissionPlanMember == null) {
                    continue;
                } else {
                    Long planId = commissionPlanMember.getPlanId();
                    // 根据方案id和套餐id获取套餐方案配置详情
                    CommissionPlanConf commissionPlanIssueConf = commissionPlanConfService.getCommissionConfByPlanIdAndPackageId(planId, commissionPackageId);
                    if (commissionPlanIssueConf == null) {
                        continue;
                    } else {
                        // 添加层级
                        PlanConfDTO planConfDTO = convertToPlanConfDTO(commissionPlanIssueConf);
                        ruleVersionDTO.setPlanConf(planConfDTO);
                    }
                }
            }

            // 2. 由上而下计算分成比例，生成多条待结算记录
            if (CollectionUtil.isNotEmpty(ruleVersions)) {
                RuleVersionDTO topPlanIssueConf = ruleVersions.get(0);
                if (topPlanIssueConf.getLevel() == 0) {
                    long all = 0L;
                    // 配置的下级分成金额
                    long remove = 0L;
                    // 配置的自留分成金额
                    long remain = 0L;
                    // 实际自留佣金
                    long actualRemain = 0L;
                    // 实际下级分成金额
                    long actualRemove = 0L;

                    for (int i = 0; i < ruleVersions.size(); i++) {
                        //  一级一级往下分
                        RuleVersionDTO ruleVersionDTO = ruleVersions.get(i);
                        PlanConfDTO issueConf = ruleVersionDTO.getPlanConf();
                        if (i == 0 && issueConf != null) {
                            all = issueConf.getChildDivide() + issueConf.getSelfDivide();
                        }
                        CommissionDTO commissionDTO = new CommissionDTO();
                        if (issueConf == null) {
                            // 分成方案为空则不往下分
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemain(all);
                            actualRemove = 0L;
                            commissionDTO.setActualRemove(actualRemove);
                        } else {
                            remove = issueConf.getChildDivide();
                            actualRemove = (all >= remove ? remove : all);
                            actualRemain = all - actualRemove;
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemove(actualRemove);
                            commissionDTO.setActualRemain(actualRemain);
                        }
                        ruleVersionDTO.setCommission(commissionDTO);
                        // 实际下级分成金额赋值到下一级的上级分配
                        all = actualRemove;
                    }
                }
            }

        } else {
            // 只有最顶层方案规则则不结算
            log.info("订单{} 的各层级方案为空", orderId);
        }

        // 3. 订单绑定规则入库

//        String ruleVersionJson = null;
//        if (CollectionUtil.isNotEmpty(ruleVersions)) {
//            ruleVersionJson = JSONUtil.toJsonStr(ruleVersions);
//        }

        OrderCommissionRule rule = new OrderCommissionRule();

        rule.setOrderId(orderId);
        rule.setCommissionType(typeEnum.getType().longValue());
        rule.setCommissionPackage(commissionPackageId);
        rule.setRuleVersion(ruleVersions);

        if (log.isInfoEnabled()) {
            log.info("订单{} 绑定的佣金方案规则为:{}", orderId, JSONUtil.toJsonStr(ruleVersions));
        }
        orderCommissionRuleService.save(rule);
        //  绑定规则结束
    }

    /**
     * 比例分佣绑定规则
     *
     * @param amount     分佣金额
     * @param orderId    订单ID
     * @param employeeId 员工ID
     * @see CommissionBizType     方案ID
     * @see CommissionPackage     套餐ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void orderScaleBindSettleRule(Long amount, Long orderId, CommissionBizType typeEnum, CommissionPackage packageEnum, Long employeeId) {
        Boolean existsFlag = orderCommissionRuleService.checkOrderRuleExists(orderId,typeEnum,packageEnum.getType());
        if (existsFlag) {
            log.info("订单{}的佣金结算规则已锁定", orderId);
            return;
        }
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = new ArrayList<>();

        if (CommissionPackage.PHONE_DOWN.getType().equals(packageEnum.getType()) || CommissionPackage.INSURANCE_COMPANY_SERVICE.getType().equals(packageEnum.getType())) {
            superEmployees = getSuperEmployeesByDown(ancestorsCode);
        } else {
            superEmployees = getSuperEmployees(ancestorsCode);
        }
        List<RuleVersionDTO> ruleVersions = convertToRuleVersion(superEmployees);
        // 转化为待入库的规则描述存储对象
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        if (ruleVersions.size() > 1) {
            for (int i = 1; i < superEmployees.size(); i++) {
                Employee superEmployee = superEmployees.get(i);
                // 根据成员id获取被分配的方案
                CommissionPlanMembers commissionPlanMember = commissionPlanMemberService.getCommissionPlanMemberByMemberId(superEmployee.getId(), typeEnum.getType().longValue());
                RuleVersionDTO ruleVersionDTO = ruleVersions.get(i - 1);
                if (commissionPlanMember == null) {
                    continue;
                } else {
                    Long planId = commissionPlanMember.getPlanId();
                    // 根据方案id和套餐id获取套餐方案配置详情
                    CommissionPlanConf commissionPlanIssueConf = commissionPlanConfService.getCommissionConfByPlanIdAndPackageId(planId, packageEnum.getType());
                    if (commissionPlanIssueConf == null) {
                        continue;
                    } else {
                        // 添加层级
                        PlanConfDTO planConfDTO = convertToPlanConfDTO(commissionPlanIssueConf);
                        ruleVersionDTO.setPlanConf(planConfDTO);
                    }
                }
            }

            // 2. 由上而下计算分成比例，生成多条待结算记录
            if (CollectionUtil.isNotEmpty(ruleVersions)) {
                RuleVersionDTO topPlanIssueConf = ruleVersions.get(0);
                if (topPlanIssueConf.getLevel() == 0) {
                    long all = amount;
                    // 配置的下级分成金额
                    long remove = 0L;
                    // 配置的自留分成金额
                    long remain = 0L;
                    // 实际自留佣金
                    long actualRemain = 0L;
                    // 实际下级分成金额
                    long actualRemove = 0L;

                    for (int i = 0; i < ruleVersions.size(); i++) {
                        //  一级一级往下分
                        RuleVersionDTO ruleVersionDTO = ruleVersions.get(i);
                        PlanConfDTO issueConf = ruleVersionDTO.getPlanConf();
                        CommissionDTO commissionDTO = new CommissionDTO();
                        if (issueConf == null) {
                            // 分成方案为空则不往下分
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemain(all);
                            actualRemove = 0L;
                            commissionDTO.setActualRemove(actualRemove);
                        } else {
                            BigDecimal childScale = issueConf.getChildScale();
                            remove = childScale.multiply(new BigDecimal(all)).longValue();
                            actualRemove = (all >= remove ? remove : all);
                            actualRemain = all - actualRemove;
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemove(actualRemove);
                            commissionDTO.setActualRemain(actualRemain);
                        }
                        ruleVersionDTO.setCommission(commissionDTO);
                        // 实际下级分成金额赋值到下一级的上级分配
                        all = actualRemove;
                    }
                }
            }

        } else {
            // 只有最顶层方案规则则不结算
            log.info("订单{} 的各层级方案为空", orderId);
        }

        // 3. 订单绑定规则入库

//        String ruleVersionJson = null;
//        if (CollectionUtil.isNotEmpty(ruleVersions)) {
//            ruleVersionJson = JSONUtil.toJsonStr(ruleVersions);
//        }
        OrderCommissionRule rule = new OrderCommissionRule();
        rule.setOrderId(orderId);
        rule.setCommissionType(typeEnum.getType().longValue());
        rule.setCommissionPackage(packageEnum.getType());
        rule.setRuleVersion(ruleVersions);

        if (log.isInfoEnabled()) {
            log.info("订单{} 绑定的佣金方案规则为:{}", orderId, JSONUtil.toJsonStr(ruleVersions));
        }
        orderCommissionRuleService.save(rule);
        //  绑定规则结束
    }

    /**
     * 根据人员层级编码获取上级人员列表，按层级升序排序
     *
     * @param ancestorsCode
     * @return
     */
    public static List<Employee> getSuperEmployees(String ancestorsCode) {
        String[] ancestors = ancestorsCode.split(",");

        List<Employee> superEmployees = new ArrayList<>();
        for (int i = 0; i < ancestors.length; i++) {
            Long empId = Long.parseLong(ancestors[i]);
            int empLevel = i;

            StringBuilder ancestorsSb = new StringBuilder();
            for (int j = 0; j <= i; j++) {

                ancestorsSb.append(ancestors[j]);
                if (j < i) {
                    ancestorsSb.append(",");
                }
            }
            String empAncestors = ancestorsSb.toString();


            Employee superEmployee = new Employee();
            superEmployee.setId(empId);
            superEmployee.setAncestors(empAncestors);
            superEmployee.setLevel(empLevel);

            superEmployees.add(superEmployee);
        }

        return superEmployees;
    }

    /**
     * 压价方案的分佣人员不是从顶层开始
     *
     * @param ancestorsCode
     * @return
     */
    public List<Employee> getSuperEmployeesByDown(String ancestorsCode) {
        List<String> employeeIdStrs = Arrays.asList(ancestorsCode.split(","));
        List<Long> employeeIds = employeeIdStrs.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Employee> employeeList = employeeMapper.selectList(Wrappers.lambdaQuery(Employee.class)
                .in(Employee::getId, employeeIds)
                .in(Employee::getCompanyType, Arrays.asList(CompanyType.CHAIN.getCode(), CompanyType.STORE.getCode()))
                .orderByAsc(Employee::getLevel)
        );
        List<Employee> superEmployees = new ArrayList<>();
        Employee superEmployee = new Employee();
        superEmployee.setId(1L);
        superEmployee.setAncestors("1");
        superEmployee.setLevel(0);
        superEmployees.add(superEmployee);
        for (Employee employee : employeeList) {
            Employee empLevel = new Employee();
            empLevel.setId(employee.getId());
            empLevel.setAncestors(employee.getAncestors());
            empLevel.setLevel(employee.getLevel());
            superEmployees.add(empLevel);
        }
        return superEmployees;
    }

    private List<RuleVersionDTO> convertToRuleVersion(List<Employee> superEmployees) {
        if (CollectionUtil.isEmpty(superEmployees)) {
            return null;
        }
        List<RuleVersionDTO> list = new ArrayList<>();
        int size = superEmployees.size();
        for (int i = 0; i < size; i++) {
            RuleVersionDTO ruleVersionDTO = employeeMapToRuleVersionDTO(superEmployees.get(i));
            if (i != size - 1) {
                ruleVersionDTO.setChildEmployeeId(superEmployees.get(i + 1).getId());
            }
            list.add(ruleVersionDTO);
        }
        return list;
    }

    private RuleVersionDTO employeeMapToRuleVersionDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        RuleVersionDTO ruleVersionDTO = new RuleVersionDTO();
        ruleVersionDTO.setEmployeeId(employee.getId());
        ruleVersionDTO.setAncestors(employee.getAncestors());
        ruleVersionDTO.setLevel(employee.getLevel());
        return ruleVersionDTO;
    }

    private PlanConfDTO convertToPlanConfDTO(CommissionPlanConf commissionPlanIssueConf) {
        if (commissionPlanIssueConf == null) {
            return null;
        }
        PlanConfDTO planConfDTO = new PlanConfDTO();
        planConfDTO.setPlanId(commissionPlanIssueConf.getPlanId());
        planConfDTO.setSuperDivide(commissionPlanIssueConf.getSuperDivide());
        planConfDTO.setChildDivide(commissionPlanIssueConf.getChildDivide());
        planConfDTO.setSelfDivide(commissionPlanIssueConf.getSelfDivide());
        planConfDTO.setSuperScale(commissionPlanIssueConf.getSuperScale());
        planConfDTO.setChildScale(commissionPlanIssueConf.getChildScale());
        planConfDTO.setSelfScale(commissionPlanIssueConf.getSelfScale());
        return planConfDTO;
    }


    /**
     * 生成待结算
     * @param orderId
     * @param typeEnum
     * @param commissionPackageId
     * @param employeeId
     * @param employAccountChangeEnum
     * @param remark
     */
    @Transactional(rollbackFor = Exception.class)
    public void waitSettleOrder(Long orderId, CommissionBizType typeEnum, Long commissionPackageId, Long employeeId, EmployAccountChangeEnum employAccountChangeEnum, String remark) {
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = new ArrayList<>();
        if (CommissionPackage.PHONE_DOWN.getType().equals(commissionPackageId) || CommissionPackage.INSURANCE_COMPANY_SERVICE.getType().equals(commissionPackageId)) {
            superEmployees = getSuperEmployeesByDown(ancestorsCode);
        } else {
            superEmployees = getSuperEmployees(ancestorsCode);
        }
        OrderCommissionRule rule = orderCommissionRuleService.getRuleVersionByOrderId(orderId,typeEnum,commissionPackageId);
        if (rule == null) {
            return;
        }
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        String versionJSON = JSONUtil.toJsonStr(rule.getRuleVersion());
        rule.setRuleVersion(JSONUtil.toList(versionJSON, RuleVersionDTO.class));
        List<RuleVersionDTO> commissionPlanIssueConfList = rule.getRuleVersion();
        // 2. 由上而下计算分成比例，生成多条待结算记录
        List<CommissionSettle> commissionSettleList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(commissionPlanIssueConfList)) {
            Date settleTime = new Date();
            int settleLevel = 0;
            for (int i = settleLevel; i < commissionPlanIssueConfList.size(); i++) {
                //  一级一级往下分
                RuleVersionDTO issueConf = commissionPlanIssueConfList.get(i);
                CommissionDTO commissionDTO = issueConf.getCommission();
                if (commissionDTO != null) {
                    // 被结算人
                    Employee settleEmp = superEmployees.get(i);
                    CommissionSettle commissionSettle = buildCommissionSettle(orderId, remark, employee, typeEnum.getType().longValue(), commissionPackageId, commissionDTO.getActualRemain().intValue(), settleTime, settleEmp, superEmployees,i);
                    commissionSettleList.add(commissionSettle);
                }
                settleLevel++;
            }
        }
        // 3. 保存入库
        if (CollectionUtil.isNotEmpty(commissionSettleList)) {
            List<CommissionSettle> commissionSettleListToSave = commissionSettleList.stream().filter(item -> item.getSettleBalance() > 0).collect(Collectors.toList());
            //有些绑定分拥规则不需要资金变化
            if(employAccountChangeEnum != null){
                //钱包资金变化
                for (CommissionSettle settle : commissionSettleListToSave) {
                    employeeAccountChangeService.changeAccount(settle.getEmployeeId(), employAccountChangeEnum, settle.getSettleBalance(), settle.getCorrelationId(), remark);
                }
            }
            this.saveBatch(commissionSettleListToSave);
        }
    }


    private CommissionSettle buildCommissionSettle(Long orderId, String remark, Employee employee, Long commissionTypeId, Long commissionPackageId, int actualRemain, Date settleTime, Employee settleEmp, List<Employee> superEmployees,int i) {
        CommissionSettle commissionSettle = new CommissionSettle();
        commissionSettle.setEmployeeId(settleEmp.getId());
//        commissionSettle.setBatchNo(settleBatchNo);
        commissionSettle.setCommissionType(commissionTypeId);
        commissionSettle.setCommissionPackage(commissionPackageId);
        commissionSettle.setSettleBalance(actualRemain);
        commissionSettle.setSettleStatus(SettleStatus.WAIT_TO_SETTLE.getStatus());
        // 关联类型 、 关联ID
        commissionSettle.setCorrelationId(orderId);
        if (employee.getLevel().compareTo(settleEmp.getLevel()) == 0) {
            commissionSettle.setGainType(CommissionSettleGainType.BY_MYSELF.getType());
        } else {
            commissionSettle.setGainType(CommissionSettleGainType.CHILD_CONTRIBUTE.getType());
            int childLevel = i + 1;
            if (childLevel < superEmployees.size()) {
                Employee settleEmpChild = superEmployees.get(childLevel);
                commissionSettle.setChildEmployeeId(settleEmpChild.getId());
            }
        }

        commissionSettle.setGainTime(settleTime);
        commissionSettle.setRemark(remark);
        commissionSettle.setLevel(settleEmp.getLevel());
        commissionSettle.setAncestors(settleEmp.getAncestors());
        commissionSettle.setCreateTime(settleTime);
        return commissionSettle;
    }

    /**
     * 订单结算
     *
     * @param orderId
     * @param typeEnum
     * @param commissionPackageId
     * @param employAccountChangeEnum
     * @param remark
     */
    @Transactional(rollbackFor = Exception.class)
    public void settleOrder(Long orderId, CommissionBizType typeEnum, Long commissionPackageId, EmployAccountChangeEnum employAccountChangeEnum, String remark) {
        LambdaQueryWrapper<CommissionSettle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommissionSettle::getCorrelationId, orderId);
        queryWrapper.eq(CommissionSettle::getSettleStatus, SettleStatus.WAIT_TO_SETTLE.getStatus());
        queryWrapper.eq(CommissionSettle::getCommissionType, typeEnum.getType());
        queryWrapper.eq(CommissionSettle::getCommissionPackage, commissionPackageId);
        List<CommissionSettle> settleList = this.baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(settleList)) return;
        for (CommissionSettle settle : settleList) {
            settle.setBatchNo(snowflakeIdService.nextId());
            settle.setSettleStatus(SettleStatus.HAVING_SETTLE.getStatus());
            settle.setSettleTime(new Date());
            settle.setRemark(remark);
            this.updateById(settle);
            employeeAccountChangeService.changeAccount(settle.getEmployeeId(), employAccountChangeEnum, settle.getSettleBalance(), settle.getCorrelationId(), remark);
        }
        /*if(!Arrays.asList(CommissionPackage.PHONE_DOWN.getType(),CommissionPackage.INSURANCE_COMPANY_SERVICE.getType()).contains(packageEnum.getType())){
            //合伙人账单数据
            commissionCheck(settleList);
        }*/
    }

    /**
     * 待结算取消
     *
     * @param orderId
     * @param typeEnum
     * @param packageEnum
     * @param employAccountChangeEnum
     * @param remark
     */
    @Transactional(rollbackFor = Exception.class)
    public void waitSettleCancel(Long orderId, CommissionBizType typeEnum, CommissionPackage packageEnum, EmployAccountChangeEnum employAccountChangeEnum, String remark) {
        LambdaQueryWrapper<CommissionSettle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommissionSettle::getCorrelationId, orderId);
        queryWrapper.eq(CommissionSettle::getSettleStatus, SettleStatus.WAIT_TO_SETTLE.getStatus());
        queryWrapper.eq(CommissionSettle::getCommissionType, typeEnum.getType());
        queryWrapper.eq(CommissionSettle::getCommissionPackage, packageEnum.getType());
        List<CommissionSettle> settleList = this.baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(settleList)) return;
        for (CommissionSettle settle : settleList) {
            //旧的已结算
            settle.setBatchNo(snowflakeIdService.nextId());
            settle.setSettleStatus(SettleStatus.HAVING_SETTLE.getStatus());
            this.updateById(settle);
            //新插入一条负数已经结算
            CommissionSettle cancelSettle = new CommissionSettle();
            BeanUtil.copyProperties(settle, cancelSettle, "id");
            cancelSettle.setSettleBalance(0 - settle.getSettleBalance());
            cancelSettle.setBatchNo(snowflakeIdService.nextId());
            cancelSettle.setSettleStatus(SettleStatus.HAVING_SETTLE.getStatus());
            this.save(cancelSettle);
            employeeAccountChangeService.changeAccount(settle.getEmployeeId(), employAccountChangeEnum, settle.getSettleBalance(), settle.getCorrelationId(), remark);
        }
    }

    /**
     * 以下开始个人收益模块
     *
     * @param bizType
     * @param employeeId
     * @return
     */
    public PersonStatDTO personStats(CommissionBizType bizType, Long employeeId) {

        PersonStatDTO personStatDTO = new PersonStatDTO();

        Long accWaitSettile = 0L;
        Long personWaitSettile = 0L;
        Long personTeamWaitSettile = 0L;
        Long personAccSettle = 0L;
        Long personTeamAccSettle = 0L;

        personWaitSettile = this.getBaseMapper().personSettleStats(employeeId, bizType.getType().intValue(), CommissionSettleGainType.BY_MYSELF.getType(), SettleStatus.WAIT_TO_SETTLE.getStatus());
        personTeamWaitSettile = this.getBaseMapper().personSettleStats(employeeId, bizType.getType().intValue(), CommissionSettleGainType.CHILD_CONTRIBUTE.getType(), SettleStatus.WAIT_TO_SETTLE.getStatus());
        accWaitSettile = personWaitSettile + personTeamWaitSettile;

        personAccSettle = this.getBaseMapper().personSettleStats(employeeId, bizType.getType().intValue(), CommissionSettleGainType.BY_MYSELF.getType(), SettleStatus.HAVING_SETTLE.getStatus());
        personTeamAccSettle = this.getBaseMapper().personSettleStats(employeeId, bizType.getType().intValue(), CommissionSettleGainType.CHILD_CONTRIBUTE.getType(), SettleStatus.HAVING_SETTLE.getStatus());


        personStatDTO.setAccWaitSettile(MoneyUtil.convert(accWaitSettile));
        personStatDTO.setPersonWaitSettile(MoneyUtil.convert(personWaitSettile));
        personStatDTO.setPersonTeamWaitSettile(MoneyUtil.convert(personTeamWaitSettile));
        personStatDTO.setPersonAccSettle(MoneyUtil.convert(personAccSettle));
        personStatDTO.setPersonTeamAccSettle(MoneyUtil.convert(personTeamAccSettle));

        return personStatDTO;
    }


    public Long userWaitSettleAcc(Long employeeId, Integer bizType) {
        return this.getBaseMapper().personSettleStats(employeeId, bizType, CommissionSettleGainType.BY_MYSELF.getType(), SettleStatus.WAIT_TO_SETTLE.getStatus());
    }


    public Page<WaitSettlesDTO> issueWaitSettles(Long employeeId, PageReq pageReq) {

        Page page = null;
        try {
            PageHelper.startPage(pageReq.getPage(), pageReq.getPageSize());
            //调用分页查询的方法
            List<WaitSettlesDTO> list = this.getBaseMapper().queryPersonSettles(employeeId, pageReq.getBizType(), CommissionSettleGainType.BY_MYSELF.getType(), SettleStatus.WAIT_TO_SETTLE.getStatus());

            page = (Page) list;
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        return page;
    }


    public Long teamWaitSettleAcc(Long employeeId, Integer bizType) {
        return this.getBaseMapper().personSettleStats(employeeId, bizType, CommissionSettleGainType.CHILD_CONTRIBUTE.getType(), SettleStatus.WAIT_TO_SETTLE.getStatus());
    }


    public Page<TeamWaitSettlesDetailDTO> issueTeamWaitSettles(Long employeeId, PageReq pageReq) {
        Page page = null;
        try {
            PageHelper.startPage(pageReq.getPage(), pageReq.getPageSize());
            //调用分页查询的方法
            List<TeamWaitSettlesDetailDTO> list = this.getBaseMapper().queryTeamSettles(employeeId, pageReq.getBizType(), CommissionSettleGainType.CHILD_CONTRIBUTE.getType(), SettleStatus.WAIT_TO_SETTLE.getStatus());
            page = (Page) list;
        } finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        return page;
    }


    /**
     * 某个员工获得的收益
     * @param amount
     * @param typeEnum
     * @param packageEnum
     * @param employeeId
     */
    public Long orderScaleByEmployee(Long amount,CommissionBizType typeEnum, CommissionPackage packageEnum, Long employeeId) {
        //  结算佣金开始
        // 1. 查找员工的层级关系，由上而下查找套餐对应的佣金方案及对应数值配置
        Employee employee = employeeMapper.selectByPrimaryKey(employeeId);
        //当前员工的人员层级编码
        String ancestorsCode = employee.getAncestors();
        List<Employee> superEmployees = new ArrayList<>();

        if (CommissionPackage.PHONE_DOWN.getType().equals(packageEnum.getType()) || CommissionPackage.INSURANCE_COMPANY_SERVICE.getType().equals(packageEnum.getType())) {
            superEmployees = getSuperEmployeesByDown(ancestorsCode);
        } else {
            superEmployees = getSuperEmployees(ancestorsCode);
        }
        List<RuleVersionDTO> ruleVersions = convertToRuleVersion(superEmployees);
        // 转化为待入库的规则描述存储对象
        // 由上而下查找套餐对应的佣金方案及对应数值配置
        if (ruleVersions.size() > 1) {
            for (int i = 1; i < superEmployees.size(); i++) {
                Employee superEmployee = superEmployees.get(i);
                // 根据成员id获取被分配的方案
                CommissionPlanMembers commissionPlanMember = commissionPlanMemberService.getCommissionPlanMemberByMemberId(superEmployee.getId(), typeEnum.getType());
                RuleVersionDTO ruleVersionDTO = ruleVersions.get(i - 1);
                if (commissionPlanMember == null) {
                    continue;
                } else {
                    Long planId = commissionPlanMember.getPlanId();
                    // 根据方案id和套餐id获取套餐方案配置详情
                    CommissionPlanConf commissionPlanIssueConf = commissionPlanConfService.getCommissionConfByPlanIdAndPackageId(planId, packageEnum.getType());
                    if (commissionPlanIssueConf == null) {
                        continue;
                    } else {
                        // 添加层级
                        PlanConfDTO planConfDTO = convertToPlanConfDTO(commissionPlanIssueConf);
                        ruleVersionDTO.setPlanConf(planConfDTO);
                    }
                }
            }

            // 2. 由上而下计算分成比例，生成多条待结算记录
            if (CollectionUtil.isNotEmpty(ruleVersions)) {
                RuleVersionDTO topPlanIssueConf = ruleVersions.get(0);
                if (topPlanIssueConf.getLevel() == 0) {
                    long all = amount;
                    // 配置的下级分成金额
                    long remove = 0L;
                    // 配置的自留分成金额
                    long remain = 0L;
                    // 实际自留佣金
                    long actualRemain = 0L;
                    // 实际下级分成金额
                    long actualRemove = 0L;

                    for (int i = 0; i < ruleVersions.size(); i++) {
                        //  一级一级往下分
                        RuleVersionDTO ruleVersionDTO = ruleVersions.get(i);
                        PlanConfDTO issueConf = ruleVersionDTO.getPlanConf();
                        CommissionDTO commissionDTO = new CommissionDTO();
                        if (issueConf == null) {
                            // 分成方案为空则不往下分
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemain(all);
                            actualRemove = 0L;
                            commissionDTO.setActualRemove(actualRemove);
                        } else {
                            BigDecimal childScale = issueConf.getChildScale();
                            remove = childScale.multiply(new BigDecimal(all)).longValue();
                            actualRemove = (all >= remove ? remove : all);
                            actualRemain = all - actualRemove;
                            commissionDTO.setAll(all);
                            commissionDTO.setActualRemove(actualRemove);
                            commissionDTO.setActualRemain(actualRemain);
                        }
                        ruleVersionDTO.setCommission(commissionDTO);
                        // 实际下级分成金额赋值到下一级的上级分配
                        all = actualRemove;
                    }
                }
            }

        } else {
           return 0L;
        }

        log.info("各层级分佣方案",JSONUtil.toJsonStr(ruleVersions));

        CommissionDTO commissionDTO = ruleVersions.stream().filter(e->e.getEmployeeId().equals(employeeId))
                .map(RuleVersionDTO::getCommission).findAny().orElse(new CommissionDTO());

        return ObjectUtil.isNotNull(commissionDTO.getActualRemain())?commissionDTO.getActualRemain():0;

    }


    private void commissionCheck(List<CommissionSettle> settleList){
        //总得分佣金额
        Integer allAmount = settleList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();

        //最后层级
        CommissionSettle maxEmployee = settleList.stream()
                .max(Comparator.comparingInt(CommissionSettle::getLevel)).get();
        String [] levels = maxEmployee.getAncestors().split(",");
        Employee region = null;
        if(levels.length>2){
            Long regionId = Long.valueOf(levels[2]);
            region = employeeMapper.selectById(regionId);
        }
        Integer regionAmount = 0;
        //合伙人直营门店的
        if(ObjectUtil.isNotNull(region) && CompanyType.COMPANY.getCode()!=region.getCompanyType().intValue()){
            List<CommissionSettle> regionList =  settleList.stream().filter(e->e.getLevel()>=2).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(regionList)){
                regionAmount = regionList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
            }
        }else{
            List<CommissionSettle> regionList =  settleList.stream().filter(e->e.getLevel()>=3).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(regionList)){
                regionAmount = regionList.stream().mapToInt(CommissionSettle::getSettleBalance).sum();
            }
        }

        CommissionSettleCheck settleCheck = new CommissionSettleCheck();
        if(levels.length>1){
            settleCheck.setBdId(Long.valueOf(levels[1]));
        }
        if(levels.length>2){
            settleCheck.setRegionId(Long.valueOf(levels[2]));
        }
        settleCheck.setCommissionType(maxEmployee.getCommissionType());
        settleCheck.setCommissionPackage(maxEmployee.getCommissionPackage());
        settleCheck.setSettleBalance(allAmount-regionAmount);
        settleCheck.setAllAmount(allAmount);
        settleCheck.setPayAmount(regionAmount);
        settleCheck.setCorrelationId(maxEmployee.getCorrelationId());
        settleCheck.setSettleTime(maxEmployee.getSettleTime());
        settleCheck.setRemark(maxEmployee.getRemark());
        commissionSettleCheckService.save(settleCheck);
    }
}