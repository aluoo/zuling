package com.anyi.sparrow.account.dao.mapper;


import com.anyi.sparrow.account.domain.EmployeeRealNameVerification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Mapper
@Repository
public interface EmployeeRealNameVerificationMapper extends BaseMapper<EmployeeRealNameVerification> {
}