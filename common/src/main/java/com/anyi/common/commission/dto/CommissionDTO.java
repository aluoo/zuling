package com.anyi.common.commission.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author shenbh
 * @since 2023/3/29
 */
@Data
public class CommissionDTO implements Serializable {

    private Long all;

    private Long actualRemove;

    private Long actualRemain;
}
