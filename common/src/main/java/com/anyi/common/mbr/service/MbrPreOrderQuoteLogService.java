package com.anyi.common.mbr.service;

import cn.hutool.core.collection.CollUtil;
import com.anyi.common.mbr.domain.MbrPreOrderQuoteLog;
import com.anyi.common.mbr.mapper.MbrPreOrderQuoteLogMapper;
import com.anyi.common.mbr.response.PreOrderQuoteLogCountDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class MbrPreOrderQuoteLogService extends ServiceImpl<MbrPreOrderQuoteLogMapper, MbrPreOrderQuoteLog>  {

    public Map<Long, PreOrderQuoteLogCountDTO> countGroupByOrderIds(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return Collections.emptyMap();
        }
        List<PreOrderQuoteLogCountDTO> list = this.baseMapper.countGroupByOrderIds(orderIds);
        return CollUtil.isEmpty(list)
                ? Collections.emptyMap()
                : list.stream().collect(
                Collectors.toMap(PreOrderQuoteLogCountDTO::getOrderId,Function.identity())
        );
    }

}
