package com.anyi.common.insurance.response;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.anyi.common.insurance.domain.dto.DiInsuranceOrderPictureDTO;
import com.anyi.common.insurance.enums.DiOrderStatusEnum;
import com.anyi.common.product.domain.dto.OrderLogDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceOrderDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private Long id;
    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty("门店员工名称")
    private String storeEmployeeName;
    @ApiModelProperty("门店员工手机号")
    private String storeEmployeeMobile;
    @ApiModelProperty("门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("门店名称")
    private String storeCompanyName;

    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;
    @ApiModelProperty("数保产品ID")
    private Long insuranceId;
    @ApiModelProperty("数保产品名称")
    private String productName;
    @ApiModelProperty("数保产品规格")
    private String productSpec;
    @ApiModelProperty("数保产品年限")
    private Integer insurancePeriod;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer productSkuRetailPrice;
    @ApiModelProperty(value = "手机市场零售价")
    private String productSkuRetailPriceStr;
    // @ApiModelProperty("数保产品保额")
    // private Long insuranceCoverPrice;
    // private String insuranceCoverPriceStr;
    @ApiModelProperty("数保产品名称")
    private String insuranceName;
    @ApiModelProperty("数保产品类型")
    private String insuranceType;

    /**
     * @see DiOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("手机串号")
    private String imeiNo;
    @ApiModelProperty("成交价")
    private Integer price;
    private String priceStr;

    @ApiModelProperty("保险服务单状态")
    private Integer insuranceStatus;
    @ApiModelProperty("保险服务单号")
    private String insuranceNo;
    @ApiModelProperty("备注")
    private String remark;

    private List<DiInsuranceOrderPictureDTO> pictures;

    @ApiModelProperty("支付状态")
    private Integer payStatus;
    @ApiModelProperty("支付类型")
    private Integer payType;

    private List<OrderLogDTO> logs;

    @ApiModelProperty("是否可以取消")
    private Boolean canCancel;
    @ApiModelProperty("是否可以退款")
    private Boolean canRefund;
    @ApiModelProperty("是否可以修改资料")
    private Boolean canEdit;

    @ApiModelProperty(value = "订单过期时间", notes = "订单过期自动关闭时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    public void setExpiredTime(int expiredMinutes) {
        if (this.status != null && this.status.equals(DiOrderStatusEnum.PENDING_PAYMENT.getCode())) {
            DateTime expiredTime = DateUtil.offset(DateUtil.date(this.createTime), DateField.MINUTE, expiredMinutes);
            this.expiredTime = expiredTime.toJdkDate();
        }
    }
}