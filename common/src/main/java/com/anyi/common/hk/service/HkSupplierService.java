package com.anyi.common.hk.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.hk.domain.HkSupplier;
import com.anyi.common.hk.mapper.HkSupplierMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class HkSupplierService extends ServiceImpl<HkSupplierMapper, HkSupplier>  {

    public Map<Long, HkSupplier> getSupplierInfoMap(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<HkSupplier> list = this.lambdaQuery().in(HkSupplier::getId, ids).list();
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(Collectors.toMap(HkSupplier::getId, Function.identity()));
    }

}
