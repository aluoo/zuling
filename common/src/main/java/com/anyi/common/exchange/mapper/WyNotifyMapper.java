package com.anyi.common.exchange.mapper;

import com.anyi.common.exchange.domain.OcpxNotify;
import com.anyi.common.exchange.domain.WyNotify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/7/30
 * @Copyright
 * @Version 1.0
 */
@Mapper
@Repository
public interface WyNotifyMapper extends BaseMapper<WyNotify> {
}