package com.anyi.common.commission.service;

import com.anyi.common.commission.domain.CommissionPlanConf;
import com.anyi.common.commission.domain.CommissionPlanMembers;
import com.anyi.common.commission.mapper.CommissionPlanConfMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CommissionPlanConfService extends ServiceImpl<CommissionPlanConfMapper, CommissionPlanConf> {

    @Autowired
    CommissionPlanMembersService planMembersService;

    public List<CommissionPlanConf> queryByPlanId(Long planId) {
        return lambdaQuery().eq(CommissionPlanConf::getPlanId, planId).list();
    }


    public Map<Long, CommissionPlanConf> getParentConf(Long bizTypeId, Long employeeIdCreate) {
        //查找到上级给自己配置的方案
        CommissionPlanMembers member = planMembersService.queryByChildEmployeeId(bizTypeId, employeeIdCreate);
        //没有配置方案
        if (member == null) {
            return new HashMap<>();
        }
        return queryByPlanId(member.getPlanId())
                .stream().collect(Collectors.toMap(CommissionPlanConf::getTypePackageId, Function.identity()));
    }


    public boolean removeByPlanId(Long planId) {
        LambdaQueryWrapper<CommissionPlanConf> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CommissionPlanConf::getPlanId, planId);
        return this.remove(deleteWrapper);
    }

    public CommissionPlanConf getCommissionConfByPlanIdAndPackageId(Long planId, Long packageInfoId) {
        LambdaQueryWrapper<CommissionPlanConf> lambdaQueryChainWrapper = new LambdaQueryWrapper<CommissionPlanConf>();
        lambdaQueryChainWrapper.eq(CommissionPlanConf::getPlanId, planId)
                .eq(CommissionPlanConf::getTypePackageId, packageInfoId)
        ;
        return this.getBaseMapper().selectOne(lambdaQueryChainWrapper);
    }

}