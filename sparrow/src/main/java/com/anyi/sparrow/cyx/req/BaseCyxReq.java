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
public class BaseCyxReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 描述: 业务请求参数的集合，最⼤⻓度
     * 不限，除公共参数外所有请求参
     * 数都必须放在这个参数中传递，
     * 具体参照各产品快速接⼊⽂档
     * 是否必填: 必填
     * 最大长度: 无限制
     */
    private String data;
    /**
     * 描述: 接⼝能⼒ID
     * 是否必填: 必填
     * 最大长度: 8
     */
    private String apiAppId;
    /**
     * 暂不⽀持
     * 是否必填: 可选
     * 最大长度: 256
     */
    private String notifyUrl;
}