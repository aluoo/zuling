package com.anyi.common.insurance.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.insurance.domain.DiInsuranceOrder;
import com.anyi.common.insurance.mapper.DiInsuranceOrderMapper;
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
 *  服务实现类
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@Service
public class DiInsuranceOrderService extends ServiceImpl<DiInsuranceOrderMapper, DiInsuranceOrder> {

    public Map<Long, DiInsuranceOrder> getInsuranceInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DiInsuranceOrder> list = this.lambdaQuery().in(DiInsuranceOrder::getId, ids).list();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(DiInsuranceOrder::getId, Function.identity()));
    }

}
