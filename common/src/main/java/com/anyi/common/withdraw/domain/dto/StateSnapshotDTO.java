package com.anyi.common.withdraw.domain.dto;

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
public class StateSnapshotDTO {

    private Integer status;

    private String remark;

    private LocalDateTime reachTime;
}