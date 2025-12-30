package com.anyi.common.insurance.mapper;

import com.anyi.common.insurance.domain.DiUserLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/9
 */
@Mapper
@Repository
public interface DiUserLoginMapper extends BaseMapper<DiUserLogin> {
}