package com.anyi.sparrow.notice.req;


import com.anyi.common.notice.domain.enums.MsgBizTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("分页信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListReq {

    @ApiModelProperty("第几页，从1开始，默认1")
    private int page = 1;

    @ApiModelProperty("每页条数，默认10")
    private int pageSize = 10;

    /**
     * @see MsgBizTypeEnum
     */
    @ApiModelProperty("comm普通消息,sysnotice系统公告,withdraw提现,active_appeal激活申诉,deposit_appeal押金申诉多个用逗号隔开")
    private String bizType;

    private Long employeeId;
}