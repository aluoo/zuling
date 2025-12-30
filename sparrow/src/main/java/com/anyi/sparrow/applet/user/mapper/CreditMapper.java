package com.anyi.sparrow.applet.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anyi.sparrow.applet.user.domain.Credit;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author peng can
 * @date 2022/12/13
 */
public interface CreditMapper extends BaseMapper<Credit> {

    int editScore(@Param("id") Long id, @Param("score") Long score, @Param("dateTime") LocalDateTime dateTime);
}
