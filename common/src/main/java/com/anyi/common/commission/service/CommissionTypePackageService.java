package com.anyi.common.commission.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.commission.domain.CommissionPlan;
import com.anyi.common.commission.domain.CommissionType;
import com.anyi.common.commission.domain.CommissionTypePackage;
import com.anyi.common.commission.dto.OverviewDTO;
import com.anyi.common.commission.mapper.CommissionPlanMapper;
import com.anyi.common.commission.mapper.CommissionTypeMapper;
import com.anyi.common.commission.mapper.CommissionTypePackageMapper;
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
public class CommissionTypePackageService extends ServiceImpl<CommissionTypePackageMapper, CommissionTypePackage> {
}