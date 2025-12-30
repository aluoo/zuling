package com.anyi.sparrow.organize.employee.dao.mapper;

import com.anyi.sparrow.organize.employee.domain.TemporaryEmployee;
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
public interface TemporaryEmployeeMapper extends BaseMapper<TemporaryEmployee> {
}