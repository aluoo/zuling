package com.anyi.common.insurance.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/23
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("数保订单资料修改请求对象")
public class InsuranceOrderEditReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单号不能为空")
    public Long orderId;

    @ApiModelProperty("客户姓名")
    @NotBlank(message = "姓名不能为空")
    private String customName;

    @ApiModelProperty("客户手机号")
    @NotBlank(message = "手机号不能为空")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    @NotBlank(message = "身份证不能为空")
    private String idCard;

    @ApiModelProperty("手机串号")
    @NotBlank(message = "IMEI不能为空")
    private String imeiNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("图片资料")
    @Valid
    private List<Picture> pictures;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("数保订单-图片资料请求对象")
    public static class Picture implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("1图片 2视频")
        private Integer type;
        @ApiModelProperty("图片地址")
        @NotBlank(message = "资料不能为空")
        private String url;
    }
}