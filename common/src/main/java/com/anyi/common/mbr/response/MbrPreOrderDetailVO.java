package com.anyi.common.mbr.response;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import com.anyi.common.mbr.enums.MbrPreOrderStatusEnum;
import com.anyi.common.mbr.enums.MbrPreOrderSubStatusEnum;
import com.anyi.common.product.domain.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenjian
 * @Description
 * @Date 2025/4/21
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbrPreOrderDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty("商品SKU ID")
    private Long productSkuId;
    @ApiModelProperty("手机商品名称")
    private String productName;
    @ApiModelProperty("手机规格名称")
    private String productSpec;
    @ApiModelProperty("1新机2二手机")
    private Integer productType;
    @ApiModelProperty("期数")
    private Integer period;
    @ApiModelProperty("客户姓名")
    private String customName;
    @ApiModelProperty("客户手机号")
    private String customPhone;
    @ApiModelProperty("客户身份证")
    private String idCard;
    @ApiModelProperty(value = "回收商ID")
    private Long recyclerCompanyId;
    @ApiModelProperty(value = "回收商名称")
    private String recyclerCompanyName;
    /**
     * @see MbrPreOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;
    /**
     * @see MbrPreOrderSubStatusEnum
     */
    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;

    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "订单过期时间", notes = "订单过期自动关闭时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    @ApiModelProperty(value = "进件单审核过期时间", notes = "超过该时间关闭进件单审核功能")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quoteExpiredTime;

    @ApiModelProperty("租机平台审核通过数")
    private Integer passNum;

    @ApiModelProperty("租机平台待审核数")
    private Integer dealNum;

    @ApiModelProperty("订单状态中文")
    private String statusName;

    @ApiModelProperty("租机平台审核列表")
    private List<MbrPlatQuoteLogVO> quoteLogList;

    public void setExpiredTime(int productOrderExpiredMinutes, int quoteExpiredMinutes) {
        if (this.getStatus() != null && this.getStatus().equals(OrderStatusEnum.UNCHECKED.getCode())) {
            this.setExpiredTime(productOrderExpiredMinutes);
            this.setQuoteExpiredTime(quoteExpiredMinutes);
        }
    }

    public void setExpiredTime(int expiredMinutes) {
        DateTime expiredTime = DateUtil.offset(DateUtil.date(this.getCreateTime()), DateField.MINUTE, expiredMinutes);
        this.expiredTime = expiredTime.toJdkDate();
    }

    public void setQuoteExpiredTime(int quoteExpiredMinutes) {
        DateTime expiredTime = DateUtil.offset(DateUtil.date(this.getCreateTime()), DateField.MINUTE, quoteExpiredMinutes);
        this.quoteExpiredTime = expiredTime.toJdkDate();
    }

    public String getStatusName(){
        return EnumUtil.getBy(MbrPreOrderStatusEnum::getCode,this.status).getDesc();
    }


}