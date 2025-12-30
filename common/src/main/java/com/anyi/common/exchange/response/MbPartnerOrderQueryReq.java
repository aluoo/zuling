package com.anyi.common.exchange.response;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class MbPartnerOrderQueryReq  implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("渠道码")
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    @ApiModelProperty("渠道订单号")
    private List<String> channelOrderNo;


}
