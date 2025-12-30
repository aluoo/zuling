package com.anyi.common.insurance.req;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel("数保报险订单-请求对象")
public class InsuranceFixOrderDTO extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("修改的时候传")
    private Long id;

    @ApiModelProperty("投保单ID")
    @NotNull(message = "投保单ID不能为空")
    private Long orderId;

    @ApiModelProperty("服务类型ID")
    @NotNull(message = "服务类型ID不能为空")
    private Long serviceType;

    @ApiModelProperty("理赔项目ID")
    @NotNull(message = "理赔项目ID不能为空")
    private Long claimItem;

    @ApiModelProperty("是否读取串号")
    @NotNull(message = "是否读取串号不能为空")
    private Boolean imeiRead;

    @ApiModelProperty("手机故障")
    private String breakDown;

    @ApiModelProperty("迭代升级1同款同型2升级迭代")
    private Integer upProduct;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("维修收款账号姓名")
    private String fixName;

    @ApiModelProperty("维修收款支付宝账号")
    private String fixAlipay;

    @ApiModelProperty("维修银行卡号")
    private String fixBankAccount;

    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("手机市场零售价")
    private String productSkuRetailPrice;

    @ApiModelProperty("旧机手机市场零售价")
    private String oldSkuRetailPrice;

    @ApiModelProperty("补交金额")
    private String suppleAmount;

    @ApiModelProperty("折抵价格")
    private String discountAmount;

    @ApiModelProperty("订单编码")
    private String orderNo;

    @ApiModelProperty("维修城市")
    private String fixCity;

    @ApiModelProperty("联系人电话号码")
    private String contactMobile;

    @ApiModelProperty("报险材料图片")
    @Valid
    private List<InsuranceFixPicture> fixPictureList;

    @ApiModelProperty("补充材料图片")
    @Valid
    private List<InsuranceFixPicture> supplePictureList;

    @ApiModelProperty("新机IMEI")
    private String newImei;

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
