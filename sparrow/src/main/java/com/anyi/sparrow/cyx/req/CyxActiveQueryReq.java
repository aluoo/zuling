package com.anyi.sparrow.cyx.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CyxActiveQueryReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 车牌号
     * 最大长度12
     * 必填
     */
    private String plateNum;

    /**
     * ⻋牌颜⾊；
     * 0 蓝色
     * 1 黄色
     */
    private String plateColor;
}