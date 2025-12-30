package com.anyi.common.insurance.req;

import com.anyi.common.domain.entity.AbstractBaseEntity;
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
 * <p>
 * 数保报修订单
 * </p>
 *
 * @author chenjian
 * @since 2024-05-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("数保报险订单理赔-请求对象")
public class InsuranceFixSettleDTO extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("报险订单ID")
    private Long id;

    @ApiModelProperty("维修收款账号姓名")
    private String fixName;

    @ApiModelProperty("维修收款支付宝账号")
    private String fixAlipay;

    @ApiModelProperty("报险材料图片")
    @Valid
    private List<InsuranceFixPicture> settlePictureList;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("数保报险订单-图片资料请求对象")
    public static class InsuranceFixPicture implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("示例图选项ID")
        @NotNull(message = "材料选项ID不能为空")
        private Long optionId;

        @ApiModelProperty("示例图选项CODE")
        @NotBlank(message = "选项CODE不能为空")
        private String code;

        @ApiModelProperty("示例图选项标题")
        @NotBlank(message = "选项标题不能为空")
        private String title;

        @ApiModelProperty("上传图片或视频")
        @NotBlank(message = "请上传图片或视频")
        private String value;

    }

}
