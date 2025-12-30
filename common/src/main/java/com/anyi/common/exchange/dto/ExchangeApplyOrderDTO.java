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
public class ExchangeApplyOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("后端自己传")
    private Long employeeId;

    @ApiModelProperty("安装记录订单号")
    private String orderSn;

    /**
     * @see ExchangeOrderTypeEnum 方案ID
     */
    @ApiModelProperty("类型5快手绿洲6苹果抖音")
    private Integer type;

    @ApiModelProperty("晒单图片信息")
    private List<ExchangeApplyOrderDTO.ApplyPicDTO> picList;

    @ApiModelProperty("来源0外部吉迅1正常晒单2晒单跳过")
    private Integer source;

    @Data
    public static class ApplyPicDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("图片地址")
        private String imageUrl;

        @ApiModelProperty("uid")
        private String uid;

        @ApiModelProperty("did")
        private String did;

        @ApiModelProperty("激活时间")
        private String actTime;

        @ApiModelProperty("渠道号")
        private String channel;

        @ApiModelProperty("IMEI")
        private String imei;

        @ApiModelProperty("imeiTwo")
        private String imeiTwo;

        @ApiModelProperty("单号")
        private String orderSn;

    }




}