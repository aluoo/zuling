package com.anyi.common.domain.mapper;

import com.anyi.common.domain.entity.CommonSysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/2
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface CommonSysDictMapper extends BaseMapper<CommonSysDict> {
}