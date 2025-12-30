package com.anyi.common.account.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 资金明细列表接口请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@Data
@ApiModel
public class QueryAccountLogReq {

    @ApiModelProperty(value = "变动类型,为空则为全部类型")
    private String typeCode;
    @ApiModelProperty(value = "查询月份", example = "2022年01月")
    private String month;
    @ApiModelProperty(value = "第几页", example = "1")
    private Integer page;
    @ApiModelProperty(value = "每页记录数", example = "20")
    private Integer pageSize;
    @ApiModelProperty(value = "看具体某个人明细时传，看自己的不用传")
    private Long employeeId;
}
