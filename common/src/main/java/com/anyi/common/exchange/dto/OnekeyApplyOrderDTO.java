package com.anyi.common.exchange.dto;

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
public class OnekeyApplyOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("后端自己传")
    private Long employeeId;

    @ApiModelProperty("晒单图片信息")
    private List<ApplyPicDTO> picList;

    @ApiModelProperty("来源")
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

    }


}