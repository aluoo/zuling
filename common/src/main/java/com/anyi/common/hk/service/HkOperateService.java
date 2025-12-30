package com.anyi.common.hk.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.hk.domain.HkOperator;
import com.anyi.common.hk.mapper.HkOperatorMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class HkOperateService extends ServiceImpl<HkOperatorMapper, HkOperator>  {

    public Map<Long, HkOperator> getOperatorInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<HkOperator> list = this.lambdaQuery().in(HkOperator::getId, ids).list();
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(HkOperator::getId, Function.identity()));
    }
}
