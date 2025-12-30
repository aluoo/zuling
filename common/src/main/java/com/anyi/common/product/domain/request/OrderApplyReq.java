package com.anyi.common.product.domain.request;

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
@ApiModel("报价订单提交请求对象")
public class OrderApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    @NotNull(message = "商品ID不能为空")
    public Long productId;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空")
    public String productName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("选中的选项信息列表")
    @Valid
    private List<CheckedOption> checkedOptions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("报价订单-选项确认请求对象")
    public static class CheckedOption implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("选项ID")
        private Long optionId;
        @ApiModelProperty("选项code")
        private String code;
        @ApiModelProperty("选项值，图片选项则值为图片地址")
        @NotBlank(message = "选项值不能为空")
        private String value;
    }
}