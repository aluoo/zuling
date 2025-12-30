package com.anyi.sparrow.assist.verify.dao.mapper;

import com.anyi.sparrow.assist.verify.domain.VerifyCode;
import org.apache.ibatis.annotations.Param;

public interface ExtVerifyCodeMapper {

    VerifyCode getLatest(@Param("mobile") String mobile, @Param("biz") String biz);

    Integer getTodayTimes(@Param("mobile") String mobile, @Param("biz") String biz);

    Integer getTotalTimes(@Param("mobile") String mobile, @Param("biz") String biz);
}