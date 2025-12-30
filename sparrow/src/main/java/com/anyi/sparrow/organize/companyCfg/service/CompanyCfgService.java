package com.anyi.sparrow.organize.companyCfg.service;

import com.anyi.sparrow.organize.companyCfg.domain.CompanyCfg;
import com.anyi.sparrow.organize.companyCfg.dao.CompanyCfgDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyCfgService {
    @Autowired
    private CompanyCfgDao dao;

    public CompanyCfg getByCmpId(Long cmpId, Long typeId) {
        CompanyCfg record = dao.getByCmpId(cmpId, typeId);
        if(record == null) {
            record = getDef(typeId);
        }
        return record;
    }

    /**
     * 获取默认配置
     * @return
     */
    public CompanyCfg getDef(Long typeId) {
        return dao.getByCmpId(-1L, typeId);
    }

}
