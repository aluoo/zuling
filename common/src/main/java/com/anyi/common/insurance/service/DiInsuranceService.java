package com.anyi.common.insurance.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.employee.domain.Employee;
import com.anyi.common.insurance.domain.DiInsurance;
import com.anyi.common.insurance.mapper.DiInsuranceMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 保险产品表 服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Service
public class DiInsuranceService extends ServiceImpl<DiInsuranceMapper, DiInsurance>  {

    public Map<Long, DiInsurance> getInsuranceInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DiInsurance> list = this.lambdaQuery().in(DiInsurance::getId, ids).list();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(DiInsurance::getId, Function.identity()));
    }


}
