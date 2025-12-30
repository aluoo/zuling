package com.anyi.common.domain.mapper;

import com.anyi.common.domain.entity.PayCallbackRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/15
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface PayCallbackRecordMapper extends BaseMapper<PayCallbackRecord> {
}