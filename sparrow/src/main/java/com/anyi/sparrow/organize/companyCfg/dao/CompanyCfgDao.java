package com.anyi.sparrow.organize.companyCfg.dao;

import com.anyi.sparrow.organize.companyCfg.dao.mapper.CompanyCfgMapper;
import com.anyi.sparrow.organize.companyCfg.domain.CompanyCfg;
import com.anyi.sparrow.organize.companyCfg.domain.CompanyCfgExample;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyCfgDao {
    @Autowired
    private CompanyCfgMapper mapper;

    public CompanyCfg getByCmpId(Long cmpId, Long typeId) {
        CompanyCfgExample example = new CompanyCfgExample();
        example.createCriteria().andCompanyIdEqualTo(cmpId).andEtcTypeIdEqualTo(typeId);
        List<CompanyCfg> records = mapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(records)) {
            return records.get(0);
        }
        return null;
    }
}
