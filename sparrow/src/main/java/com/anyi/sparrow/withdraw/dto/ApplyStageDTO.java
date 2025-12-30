package com.anyi.sparrow.withdraw.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/4/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyStageDTO {

    private String title;
    private boolean reachFlag;
    private LocalDateTime reachTime;
    private String failMsg;

    public Long getReachTime() {
        if (reachTime != null) {
            return LocalDateTimeUtil.toEpochMilli(reachTime);
        }
        return null;
    }
}
