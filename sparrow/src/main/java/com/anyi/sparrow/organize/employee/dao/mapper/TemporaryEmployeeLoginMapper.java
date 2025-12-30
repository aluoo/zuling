package com.anyi.sparrow.organize.employee.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anyi.sparrow.organize.employee.domain.TemporaryEmployeeLogin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/9
 */
@Mapper
@Repository
public interface TemporaryEmployeeLoginMapper extends BaseMapper<TemporaryEmployeeLogin> {
}