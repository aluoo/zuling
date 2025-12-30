package com.anyi.common.commission.service;

import com.anyi.common.commission.domain.CommissionPlan;
import com.anyi.common.commission.domain.CommissionPlanMembers;
import com.anyi.common.commission.mapper.CommissionPlanMapper;
import com.anyi.common.commission.mapper.CommissionPlanMembersMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CommissionPlanMembersService extends ServiceImpl<CommissionPlanMembersMapper, CommissionPlanMembers> {

    public List<CommissionPlanMembers> queryByPlanId(Long planId) {
        return lambdaQuery()
                .eq(CommissionPlanMembers::getPlanId, planId)
                .list();
    }

    public CommissionPlanMembers queryByChildEmployeeId(Long bizTypeId, long childEmployeeId) {
        return lambdaQuery()
                .eq(CommissionPlanMembers::getTypeId, bizTypeId)
                .eq(CommissionPlanMembers::getChildEmployeeId, childEmployeeId)
                .one();
    }

    public CommissionPlanMembers queryByChild(Long childEmployeeId, Long bizTypeId) {
        return lambdaQuery()
                .eq(CommissionPlanMembers::getChildEmployeeId, childEmployeeId)
                .eq(CommissionPlanMembers::getTypeId, bizTypeId)
                .one();

    }


    public boolean removeByPlanId(long planId) {
        LambdaQueryWrapper<CommissionPlanMembers> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CommissionPlanMembers::getPlanId, planId);
        return super.remove(deleteWrapper);
    }

    public boolean removeByPlanId(long planId, List<Long> childEmployeeIds) {
        LambdaQueryWrapper<CommissionPlanMembers> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(CommissionPlanMembers::getPlanId, planId)
                .in(CommissionPlanMembers::getChildEmployeeId, childEmployeeIds);
        return super.remove(deleteWrapper);
    }

    public CommissionPlanMembers getCommissionPlanMemberByMemberId(Long id, Long bizTypeId) {
        LambdaQueryWrapper<CommissionPlanMembers> lambdaQueryChainWrapper = new LambdaQueryWrapper<CommissionPlanMembers>();
        lambdaQueryChainWrapper.eq(CommissionPlanMembers::getTypeId, bizTypeId)
                .eq(CommissionPlanMembers::getChildEmployeeId, id);
        return this.getBaseMapper().selectOne(lambdaQueryChainWrapper);
    }


}