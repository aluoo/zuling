package com.anyi.sparrow.assist.verify.dao;

import com.anyi.sparrow.assist.verify.dao.mapper.ExtVerifyCodeMapper;
import com.anyi.sparrow.assist.verify.domain.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VerifyDao {
    @Autowired
    private ExtVerifyCodeMapper extVerifyCodeMapper;

    public VerifyCode getLatest(String mobile, String biz) {
        return extVerifyCodeMapper.getLatest(mobile, biz);
    }

    public Integer getTodayTimes(String mobile, String biz) {
        return extVerifyCodeMapper.getTodayTimes(mobile, biz);
    }

    public Integer getTotalTimes(String mobile, String biz) {
        return extVerifyCodeMapper.getTotalTimes(mobile, biz);
    }
}
