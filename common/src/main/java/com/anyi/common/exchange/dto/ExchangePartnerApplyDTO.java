package com.anyi.common.exchange.dto;

import com.anyi.common.exchange.enums.ExchangeOrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangePartnerApplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("后端自己传")
    private Long employeeId;

    @ApiModelProperty("渠道码,平台方提供")
    private String channelCode;

    @ApiModelProperty("渠道方自己的唯一订单号")
    private String channelOrderNo;

    @ApiModelProperty("类型5快手绿洲6苹果抖音")
    private Integer type;

    @ApiModelProperty("来源0外部吉迅1正常晒单2晒单跳过")
    private Integer source;

    @ApiModelProperty("晒单图片信息")
    private List<ExchangePartnerApplyDTO.ApplyPicDTO> picList;

    @Data
    public static class ApplyPicDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("图片地址")
        private String imageUrl;

    }




}