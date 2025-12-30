package com.anyi.common.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("分页信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReq {

    @ApiModelProperty("第几页，从1开始，默认1")
    private int page = 1;

    @ApiModelProperty("每页条数，默认10")
    private int pageSize = 10;

    @ApiModelProperty("1服务费2拉新3手机回收4享转数保")
    private Integer bizType;
}
