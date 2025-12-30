package com.anyi.common.address.mapper;


import cn.hutool.core.collection.CollUtil;
import com.anyi.common.address.domain.LatestUseAddress;
import com.anyi.common.address.domain.LatestUseAddressExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LatestAddressDao {
    @Autowired
    private LatestUseAddressMapper mapper;

    public int insert(LatestUseAddress record) {
        return mapper.insertSelective(record);
    }

    public int update(LatestUseAddress record) {
        LatestUseAddressExample example = new LatestUseAddressExample();
        example.createCriteria().andReqEmpIdEqualTo(record.getReqEmpId())
                .andRspEmpIdEqualTo(record.getRspEmpId())
                .andBizEqualTo(record.getBiz());
        return mapper.updateByExampleSelective(record, example);
    }

    public LatestUseAddress getLatest(String biz, Long reqEmpId, Long rspEmpId) {
        LatestUseAddressExample example = new LatestUseAddressExample();
        example.createCriteria().andReqEmpIdEqualTo(reqEmpId)
                .andRspEmpIdEqualTo(rspEmpId)
                .andBizEqualTo(biz);
        List<LatestUseAddress> records = mapper.selectByExample(example);
        if (CollUtil.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }
}
