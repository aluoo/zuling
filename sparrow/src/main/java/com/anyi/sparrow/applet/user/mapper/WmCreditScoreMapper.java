package com.anyi.sparrow.applet.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anyi.sparrow.applet.user.domain.WmCreditScore;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author shenbinhong
 * @date 2023-05-16
 */
public interface WmCreditScoreMapper extends BaseMapper<WmCreditScore> {

    int editScore(@Param("id") Long id, @Param("score") Long score,@Param("score") Long  tempScore, @Param("dateTime") LocalDateTime dateTime);

}
