package com.anyi.common.commission.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import com.anyi.common.commission.domain.*;
import com.anyi.common.commission.dto.*;
import com.anyi.common.commission.mapper.CommissionPlanMapper;
import com.anyi.common.commission.req.PlanReq;
import com.anyi.common.commission.req.UpdatePlanReq;
import com.anyi.common.dept.domain.Dept;
import com.anyi.common.dept.service.DeptService;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.employee.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
@Slf4j
public class CommissionPlanService extends ServiceImpl<CommissionPlanMapper, CommissionPlan> {

    @Autowired
    CommissionPlanMembersService planMembersService;
    @Autowired
    CommissionTypeService bizTypeService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    CommissionPlanConfService planConfService;
    @Autowired
    CommissionPlanLogService planLogService;
    @Autowired
    CommissionTypePackageService packageInfoService;
    @Autowired
    DeptService deptService;

    public List<PlanDTO> planList(Long employeeId, Long bizTypeId) {
        List<PlanDTO> resultList = new ArrayList<>();
        List<CommissionPlan> plansAll = lambdaQuery().eq(CommissionPlan::getTypeId, bizTypeId)
                .eq(CommissionPlan::getEmployeeId, employeeId).orderByDesc(CommissionPlan::getCreateTime).list();
        if (CollUtil.isEmpty(plansAll)) {
            return resultList;
        }
        return plansAll.stream().map(plan -> {
            PlanDTO planDTO = new PlanDTO();
            planDTO.setPlanId(plan.getId());
            planDTO.setPlanName(plan.getName());
            planDTO.setBizTypeId(bizTypeId);
            List<CommissionPlanMembers> members = planMembersService.queryByPlanId(plan.getId());
            planDTO.setMembers(CollUtil.isNotEmpty(members) ? members.size() : 0);
            return planDTO;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(PlanReq planReq) {
        log.info("【创建佣金方案】启动");
        CommissionType commissionBizType = bizTypeService.getById(planReq.getBizTypeId());
        if (ObjectUtil.isNull(commissionBizType)) {
            throw new BusinessException(99999, "佣金方案类型不存在");
        }
        Employee employee = employeeService.getById(planReq.getEmployeeId());
        //判断方案名称重复
        this.checkPlanName(employee.getId(), 0, planReq.getPlanName(), planReq.getBizTypeId());
        //创建方案
        CommissionPlan plan = new CommissionPlan();
        plan.setTypeId(planReq.getBizTypeId());
        plan.setName(planReq.getPlanName());
        //创建来源(0-后台、1-app)'
        plan.setSourceType((byte) 1);
        plan.setEmployeeId(employee.getId());
        plan.setLevel(employee.getLevel());
        plan.setIsLeaf(employee.getLevel() == 4);
        this.save(plan);
        log.info("【创建佣金方案】方案id：{},生成成功", plan.getId());
        //方案-下级关系
        List<Long> members = planReq.getMembers();
        List<PlanReq.Conf> confs = planReq.getPlanConf();

        if (!CollectionUtils.isEmpty(confs)) {

            List<CommissionPlanConf> issueConfs = confs.stream().map(conf -> {
                CommissionPlanConf issueConf = new CommissionPlanConf();
                issueConf.setPlanId(plan.getId());
                issueConf.setAncestors(employee.getAncestors());
                issueConf.setLevel(employee.getLevel());
                // getDefaut 这个方法应该返回 发行佣金配置表的id，后续接口再回传
                // 根据回传的发行佣金配置表的id，去查询上层的分佣
                //需要判断用户是不是安逸平台这边
                if (employee.getLevel() == 0) {
                    log.info("安逸平台用户");
                    Long maxPublishFee = packageInfoService.getById(conf.getPackageInfoId()).getMaxCommissionFee();
                    //给一级渠道配置的值不得超过创建套餐时候指定的最大分佣值
                    if (conf.getChildDivide() > maxPublishFee) {
                        throw new BusinessException(BizError.COMMISSION_CONF_MAX_LIMIT);
                    }
                    issueConf.setTypePackageId(conf.getPackageInfoId());
                    issueConf.setSuperDivide(0L);
                    issueConf.setChildDivide(conf.getChildDivide());
                    issueConf.setSelfDivide(0L);
                    issueConf.setSuperScale(BigDecimal.ZERO);
                    issueConf.setSelfScale(BigDecimal.ZERO);
                    issueConf.setChildScale(conf.getChildScale());
                } else {
                    CommissionPlanConf planIssueConfParent = planConfService.getById(conf.getConfId());
                    if (planIssueConfParent == null) {
                        log.info("【】推广套餐配置：{}不存在", conf.getConfId());
                        throw new BusinessException(BizError.COMMISSION_CONF_NOT_EXIST, "" + conf.getConfId());
                    }
                    issueConf.setTypePackageId(planIssueConfParent.getTypePackageId());
                    long superDivide = planIssueConfParent.getChildDivide();
                    issueConf.setSuperDivide(superDivide);
                    issueConf.setChildDivide(conf.getChildDivide());
                    long selfDivide = superDivide - conf.getChildDivide();
                    if (selfDivide < 0) {
                        throw new BusinessException(BizError.COMMISSION_CONF_MAX);
                    }
                    issueConf.setSelfDivide(selfDivide);
                    issueConf.setSuperScale(BigDecimal.ZERO);
                    issueConf.setChildScale(conf.getChildScale());
                    issueConf.setSelfScale(new BigDecimal(1).subtract(issueConf.getChildScale()));
                }
                return issueConf;

            }).collect(Collectors.toList());

            planConfService.saveBatch(issueConfs);
            log.info("【创建佣金方案】发行佣金配置表,生成成功");
        }

        //需要排查已经被配置过的人员,没有配置过的才符合条件
        if (!CollectionUtils.isEmpty(members)) {
            //方法内已经有保存方案历史记录的调用了
            this.addMembers(employee.getId(), plan.getId(), members);
        } else {
            //保存到方案历史记录
            planLogService.backUpPlan(plan.getId());
        }

    }

    public void checkPlanName(long employeeIdCreate, long planId, String planName, Long bizTypeId) {
        boolean planNameExist = lambdaQuery()
                .eq(CommissionPlan::getName, planName)
                .eq(CommissionPlan::getEmployeeId, employeeIdCreate)
                .eq(CommissionPlan::getTypeId, bizTypeId)
                .ne(planId > 0, CommissionPlan::getId, planId)
                .exists();
        if (planNameExist) {
            log.info("【创建佣金方案】方案名称重复:{}", planName);
            throw new BusinessException(BizError.COMMISSION_PLANNAME_REPEAT, planName);
        }
    }


    public void addMembers(Long employeeId, Long planId, List<Long> addMembers) {
        //判断方案跟创建人是否一致
        CommissionPlan plan = checkPlan(employeeId, planId);
        if (plan == null) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_NOT_MATCH);
        }
        //需要排查已经被配置过的人员,没有配置过的才符合条件
        boolean anyMatch = addMembers.stream().anyMatch(memberId -> {
            CommissionPlanMembers memberCheck = planMembersService.queryByChildEmployeeId(plan.getTypeId(), memberId);
            if (memberCheck != null) {
                log.info("【创建佣金方案】，用户id：{},已经被配置过方案了", memberId);
                return true;
            }
            return false;
        });
        if (anyMatch) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_REPEATE);
        }
        //操作人的直接下级
        List<Long> employeesChildIds = employeeService.queryChildEmployees(employeeId).stream().map(Employee::getId).collect(Collectors.toList());
        boolean allMatch = new HashSet<>(employeesChildIds).containsAll(addMembers);
        if (!allMatch) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_NOT_DIRECT);
        }
        List<CommissionPlanMembers> membersNew = addMembers.stream().map(memberId -> {
            CommissionPlanMembers memberAdd = new CommissionPlanMembers();
            memberAdd.setPlanId(planId);
            memberAdd.setTypeId(plan.getTypeId());
            memberAdd.setChildEmployeeId(memberId);
            return memberAdd;
        }).collect(Collectors.toList());
        planMembersService.saveBatch(membersNew);
        //备份方案表
        planLogService.backUpPlan(planId);
    }

    public CommissionPlan checkPlan(long employeeIdCreate, long planId) {
        return lambdaQuery().eq(CommissionPlan::getId, planId).eq(CommissionPlan::getEmployeeId, employeeIdCreate).one();
    }


    public List<PlanIssueConfDTO> detail(Integer currentLevel, Long employeeId, Long planId) {

        long employeeIdCreate = employeeId;

        CommissionPlan commissionPlan = lambdaQuery().eq(CommissionPlan::getId, planId).eq(CommissionPlan::getEmployeeId, employeeIdCreate).one();
        if (commissionPlan == null) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_MATCH);
        }
        //获取最新上级修改的佣金
        Map<Long, CommissionPlanConf> parentConfsMap = null;
        if (currentLevel != 0) {
            parentConfsMap = planConfService.getParentConf(commissionPlan.getTypeId(), employeeIdCreate);
        } else {
            parentConfsMap = new HashMap<>();
        }

        List<CommissionPlanConf> issueConfs = planConfService.queryByPlanId(planId);
        Map<Long, CommissionPlanConf> finalParentConfsMap = parentConfsMap;

        //方案内已经配置的套餐
        List<PlanIssueConfDTO> confInPlan = issueConfs.stream()
                .map(issueConf -> {
                    PlanIssueConfDTO planIssueConfDTO = new PlanIssueConfDTO();
                    planIssueConfDTO.setConfId(issueConf.getId());
                    planIssueConfDTO.setPackageInfoId(issueConf.getTypePackageId());
                    planIssueConfDTO.setChildDivide(issueConf.getChildDivide());
                    planIssueConfDTO.setChildScale(issueConf.getChildScale());
                    planIssueConfDTO.setSuperDivide(issueConf.getSuperDivide());
                    planIssueConfDTO.setSuperScale(issueConf.getSuperScale());
                    planIssueConfDTO.setSelfDivide(issueConf.getSelfDivide());
                    planIssueConfDTO.setSelfScale(issueConf.getSelfScale());
                    //获取最新上级修改的佣金并显示
                    if (currentLevel != 0) {
                        Long childNewDivide = 0L;
                        if (CollectionUtil.isNotEmpty(finalParentConfsMap) && finalParentConfsMap.containsKey(issueConf.getTypePackageId())) {
                            childNewDivide = finalParentConfsMap.get(issueConf.getTypePackageId()).getChildDivide();
                        }
                        planIssueConfDTO.setSuperDivide(childNewDivide);
                    } else {
                        Long maxPublishFee = packageInfoService.getById(issueConf.getTypePackageId()).getMaxCommissionFee();
                        planIssueConfDTO.setSuperDivide(maxPublishFee);
                    }
                    CommissionTypePackage packageInfo = packageInfoService.getById(issueConf.getTypePackageId());
                    planIssueConfDTO.setPackageInfoName(packageInfo.getName());
                    planIssueConfDTO.setType(packageInfo.getType());
                    return planIssueConfDTO;
                }).collect(Collectors.toList());

        return confInPlan;
    }

    @Transactional
    public void update(UpdatePlanReq planReq) {
        Employee employee = employeeService.getById(planReq.getEmployeeId());
        //判断方案跟创建人是否一致
        CommissionPlan plan = checkPlan(employee.getId(), planReq.getPlanId());
        if (plan == null) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_NOT_MATCH);
        }
        //判断方案名称重复
        this.checkPlanName(employee.getId(), planReq.getPlanId(), planReq.getPlanName(), planReq.getBizTypeId());

        CommissionPlan updatePlan = new CommissionPlan();
        updatePlan.setId(planReq.getPlanId());
        updatePlan.setName(planReq.getPlanName());
        super.updateById(updatePlan);
        log.info("【更新方案】{}", JSONUtil.toJsonStr(updatePlan));

        //安逸平台给渠道配置
        if (employee.getLevel() == 0) {
            if (!CollectionUtils.isEmpty(planReq.getPlanConf())) {
                List<CommissionPlanConf> newPlanIssueConf = planReq.getPlanConf().stream().map(conf -> {
                    CommissionPlanConf confNew = new CommissionPlanConf();
                    confNew.setTypePackageId(conf.getPackageInfoId());
                    confNew.setSuperDivide(0L);
                    confNew.setChildDivide(conf.getChildDivide());
                    confNew.setSelfDivide(0L);
                    confNew.setSuperScale(BigDecimal.ZERO);
                    confNew.setSelfScale(BigDecimal.ZERO);
                    confNew.setChildScale(conf.getChildScale());
                    confNew.setPlanId(planReq.getPlanId());
                    confNew.setAncestors(employee.getAncestors());
                    confNew.setLevel(employee.getLevel());
                    return confNew;
                }).collect(Collectors.toList());

                planConfService.removeByPlanId(planReq.getPlanId());
                planConfService.saveBatch(newPlanIssueConf);
            }
        } else {
            //非安逸平台人员-更新配置信息
            if (!CollectionUtils.isEmpty(planReq.getPlanConf())) {
                //获取最新上级修改的佣金
                Map<Long, CommissionPlanConf> parentConfsMap = planConfService.getParentConf(planReq.getBizTypeId(), employee.getId());
                ;

                List<CommissionPlanConf> planIssueConfsUpdate = planReq.getPlanConf().stream().map(confUpdate -> {
                    CommissionPlanConf planIssueConfParent = planConfService.getById(confUpdate.getConfId());

                    if (planIssueConfParent == null) {
                        log.info("【】推广套餐配置：{}不存在", confUpdate.getConfId());
                        throw new BusinessException(BizError.COMMISSION_CONF_NOT_EXIST, "" + confUpdate.getConfId());
                    }
                    //用上级最新修改的值来分配
                    //long superDivideOld = planIssueConfParent.getSuperDivide();
                    long superDivideLastest = parentConfsMap.get(confUpdate.getPackageInfoId()).getChildDivide();
                    long childDivedeNew = confUpdate.getChildDivide();
                    if (childDivedeNew > superDivideLastest) {
                        throw new BusinessException(BizError.COMMISSION_CONF_MAX);
                    }
                    planIssueConfParent.setChildDivide(childDivedeNew);
                    long selfDivide = superDivideLastest - confUpdate.getChildDivide();
                    planIssueConfParent.setSelfDivide(selfDivide);
                    planIssueConfParent.setSuperDivide(superDivideLastest);
                    //比例
                    planIssueConfParent.setSuperScale(BigDecimal.ZERO);
                    planIssueConfParent.setChildScale(confUpdate.getChildScale());
                    planIssueConfParent.setSelfScale(new BigDecimal(1).subtract(confUpdate.getChildScale()));
                    return planIssueConfParent;
                }).collect(Collectors.toList());

                planConfService.updateBatchById(planIssueConfsUpdate);
                log.info("【更新方案】更新配置{}", JSONUtil.toJsonStr(planIssueConfsUpdate));

            }
        }
        //备份方案历史记录
        planLogService.backUpPlan(planReq.getPlanId());
    }

    @Transactional
    public boolean delete(Long employeeId, Long planId) {

        CommissionPlan commissionPlan = lambdaQuery().eq(CommissionPlan::getId, planId).eq(CommissionPlan::getEmployeeId, employeeId).one();

        if (commissionPlan == null) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_DELETE);
        }
        //备份方案表
        planLogService.backUpPlan(planId);
        //删除方案
        this.removeById(planId);
        //删除方案-人员
        planMembersService.removeByPlanId(planId);
        //删除配置
        return planConfService.removeByPlanId(planId);
    }


    public MemberDTO members(Long employeeId, Long planId) {
        //判断方案跟创建人是否一致
        CommissionPlan plan = checkPlan(employeeId, planId);
        if (plan == null) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_NOT_MATCH);
        }
        List<EmployeeDTO> employeeDTOS = planMembersService.queryByPlanId(planId).stream().map(m -> {

            Employee employee = employeeService.getById(m.getChildEmployeeId());
            EmployeeDTO employeeDTO = new EmployeeDTO();

            employeeDTO.setName(employee.getName());
            employeeDTO.setMemberId(employee.getId());
            employeeDTO.setMobile(employee.getMobileNumber());
            Dept dept = deptService.getById(employee.getDeptId());
            if (dept != null) {
                employeeDTO.setDeptName(dept.getName());
            }
            employeeDTO.setHasPlan(true);
            employeeDTO.setDeptFlag(employee.getType() == 1 || employee.getType() == 3);
            employeeDTO.setChannelFlag(employee.getType() == 1);
            return employeeDTO;

        }).collect(Collectors.toList());

        int memberNum = employeeDTOS.size();
        int deptNum = (int) employeeDTOS.stream().map(EmployeeDTO::getDeptName).distinct().count();

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberNum(memberNum);
        memberDTO.setDeptNum(deptNum);
        memberDTO.setMembers(employeeDTOS);

        return memberDTO;

    }

    public void removeMembers(Long employeeId, Long planId, List<Long> delMembers) {
        //判断方案跟创建人是否一致
        CommissionPlan plan = checkPlan(employeeId, planId);
        if (plan == null) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_NOT_MATCH);
        }

        planMembersService.removeByPlanId(planId, delMembers);

        //备份方案表
        planLogService.backUpPlan(planId);
    }

    public List<CmPlanPackageDTO> getChildIssueConf(Long employeeId, Long bizTypeId) {
        CommissionPlanMembers member = planMembersService.queryByChild(employeeId, bizTypeId);
        if (ObjectUtil.isNull(member)) {
            throw new BusinessException(BizError.COMMISSION_MEMBER_PLAN_NOT_EXIST);
        }
        List<CommissionPlanConf> confs = planConfService.queryByPlanId(member.getPlanId());
        return confs.stream().map(conf -> {
            CmPlanPackageDTO planPackageDTO = new CmPlanPackageDTO();
            planPackageDTO.setConfId(conf.getId());
            planPackageDTO.setPlanId(conf.getPlanId());
            planPackageDTO.setPackageInfoId(conf.getTypePackageId());
            //创建人的下级分成就是要再拿出来分的金额
            planPackageDTO.setSuperDivide(conf.getChildDivide());
            CommissionTypePackage packageInfo = packageInfoService.getById(conf.getTypePackageId());
            planPackageDTO.setPackageInfoName(packageInfo.getName());
            planPackageDTO.setType(packageInfo.getType());
            return planPackageDTO;
        }).collect(Collectors.toList());

    }

    public List<CmPlanPackageDTO> getAdminChildIssueConf(Long employeeId, Long bizTypeId) {
        Employee employee = employeeService.getById(employeeId);
        if (employee.getLevel() != 0) {
            throw new BusinessException(BizError.COMMISSION_NOT_ADMIN);
        }
        return packageInfoService.lambdaQuery()
                .eq(CommissionTypePackage::getDeleted, 0)
                .eq(CommissionTypePackage::getTypeId, bizTypeId)
                .orderByAsc(CommissionTypePackage::getId)
                .list().stream().map(packageInfo -> {
                    CmPlanPackageDTO planPackageDTO = new CmPlanPackageDTO();
                    planPackageDTO.setPackageInfoId(packageInfo.getId());
                    planPackageDTO.setPackageInfoName(packageInfo.getName());
                    planPackageDTO.setSuperDivide(packageInfo.getMaxCommissionFee());
                    planPackageDTO.setBizTypeId(packageInfo.getTypeId());
                    planPackageDTO.setType(packageInfo.getType());
                    return planPackageDTO;
                }).collect(Collectors.toList());
    }

    public MemberDTO childMembers(Long employeeId, Long bizTypeId) {
        List<Employee> employees = employeeService.queryChildEmployees(employeeId);

        List<EmployeeDTO> employeeDTOS = employees.stream().map(employee -> {
            EmployeeDTO employeeDTO = BeanUtil.copyProperties(employee, EmployeeDTO.class);
            employeeDTO.setMemberId(employee.getId());
            employeeDTO.setMobile(employee.getMobileNumber());
            CommissionPlanMembers member = planMembersService.queryByChildEmployeeId(bizTypeId, employee.getId());
            if (member != null) {
                employeeDTO.setHasPlan(true);
                employeeDTO.setPlanId(member.getPlanId());
                //方案名称
                CommissionPlan plan = super.getById(member.getPlanId());
                employeeDTO.setPlanName(plan.getName());
            }
            //部门
            Dept dept = deptService.getById(employee.getDeptId());
            if (dept != null) {
                employeeDTO.setDeptName(dept.getName());
            }
            employeeDTO.setDeptFlag(employee.getType() == 1 || employee.getType() == 3);
            employeeDTO.setChannelFlag(employee.getType() == 1);
            return employeeDTO;

        }).sorted(
                //1,无方案的账号靠前 2,管理员账号靠前；3,按级别从高到低；
                Comparator.comparing(EmployeeDTO::isHasPlan, Comparator.naturalOrder())
                        .thenComparing(EmployeeDTO::isDeptFlag, Comparator.reverseOrder())
//                        .thenComparing(EmployeeDTO::getLevel, Comparator.reverseOrder())
        ).collect(Collectors.toList());

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberNum(employeeDTOS.size());
        memberDTO.setMembers(employeeDTOS);

        return memberDTO;
    }


}