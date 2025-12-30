package com.anyi.common.exchange.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 拉新安装包
 * </p>
 *
 * @author chenjian
 * @since 2024-04-07
 */
@Data
public class MbPartnerOrderQueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<orderStatus> orderList;

    @Data
    public static class orderStatus implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("订单状态")
        private Integer status;

        @ApiModelProperty("备注说明")
        private String remark;

        @ApiModelProperty("渠道订单号")
        private String channelOrderNo;

    }

}
