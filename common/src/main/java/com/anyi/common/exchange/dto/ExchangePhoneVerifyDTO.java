package com.anyi.common.exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangePhoneVerifyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("审核结果1通过-1不通过")
    private Integer status;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("备注")
    private String remark;




}