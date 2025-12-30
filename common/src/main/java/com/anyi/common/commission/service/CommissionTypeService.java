package com.anyi.common.commission.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.commission.domain.CommissionPlan;
import com.anyi.common.commission.domain.CommissionType;
import com.anyi.common.commission.dto.OverviewDTO;
import com.anyi.common.commission.mapper.CommissionTypeMapper;
import com.anyi.common.commission.mapper.CommissionPlanMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CommissionTypeService extends ServiceImpl<CommissionTypeMapper, CommissionType> {
    @Autowired
    CommissionPlanMapper commissionPlanMapper;

    public List<OverviewDTO> overview(Long employeeId) {
        List<OverviewDTO> resultList = new ArrayList<>();
        List<CommissionType> bizTypeList = this.list(
                Wrappers.lambdaQuery(CommissionType.class).eq(CommissionType::getDeleted, 0));
        if (CollUtil.isEmpty(bizTypeList)) {
            return resultList;
        }

        for (CommissionType type : bizTypeList) {
            OverviewDTO overviewDTO = new OverviewDTO();
            long num = commissionPlanMapper.selectCount(Wrappers.lambdaQuery(CommissionPlan.class)
                    .eq(CommissionPlan::getEmployeeId, employeeId)
                    .eq(CommissionPlan::getTypeId, type.getId()));
            overviewDTO.setBizTypeId(type.getId());
            overviewDTO.setBizTypeName(type.getName());
            overviewDTO.setNum((int) num);
            resultList.add(overviewDTO);
        }
        return resultList;
    }

}