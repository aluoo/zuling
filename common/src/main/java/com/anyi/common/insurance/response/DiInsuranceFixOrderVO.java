package com.anyi.common.insurance.response;

import com.anyi.common.domain.entity.AbstractBaseEntity;
import com.anyi.common.insurance.domain.DiInsuranceFixOrderOption;
import com.anyi.common.insurance.domain.DiOption;
import com.anyi.common.util.MoneyUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.convert.DataSizeUnit;

import java.io.Serializable;
import java.util.Date;
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
public class DiInsuranceFixOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("投保单ID")
    private Long orderId;

    @ApiModelProperty("订单唯一码")
    private String orderNo;

    @ApiModelProperty("手机商品名称")
    private String productName;

    @ApiModelProperty("手机规格名称")
    private String productSpec;

    @ApiModelProperty("手机串号")
    private String imeiNo;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customPhone;

    @ApiModelProperty("客户身份证")
    private String idCard;

    @ApiModelProperty("服务产品")
    private String insuranceName;

    @ApiModelProperty("产品年限 0表示终身")
    private Integer insurancePeriod;

    @ApiModelProperty("0安卓1Ios")
    private Integer mobileType;

    @ApiModelProperty("服务类型")
    DiOption serviceType;

    @ApiModelProperty("理赔项目")
    List<DiOption> fixItemList;

    @ApiModelProperty("报险材料示例图集合")
    List<InsuranceDemoImageVO> fixDataList;

    @ApiModelProperty("补充材料示例图集合")
    List<InsuranceDemoImageVO> suppleDataList;

    @ApiModelProperty("理赔材料示例图集合")
    List<InsuranceDemoImageVO> settleDataList;

    @ApiModelProperty("折抵金额")
    private Integer discountAmount;

    @ApiModelProperty("折抵金额描述")
    private String discountAmountDesc;

    @ApiModelProperty("旧机价格")
    private Integer oldSkuRetailPrice;

    @ApiModelProperty("坏机默认补差价格")
    private Integer badFixInsurancePrice;

    @ApiModelProperty("好机默认补差价格")
    private Integer goodFixInsurancePrice;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("状态中文")
    private String statusName;

    @ApiModelProperty("下单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("失效时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiredTime;

    @ApiModelProperty("我要报修,修改报修申请")
    private Boolean fixButton;
    @ApiModelProperty("上传理赔资料按钮")
    private Boolean saveDataButton;
    @ApiModelProperty("修改理赔资料按钮")
    private Boolean updateDataButton;

    @ApiModelProperty("迭代手机商品名称")
    private String newProductName;
    @ApiModelProperty("迭代手机规格名称")
    private String newProductSpec;

    @ApiModelProperty("迭代手机SKUID")
    private Long productSkuId;

    @ApiModelProperty("迭代升级手机市场零售价")
    private Integer newProductSkuRetailPrice;
    @ApiModelProperty("补交金额")
    private Integer suppleAmount;

    @ApiModelProperty("理赔金额")
    private Integer settleAmount;

    @ApiModelProperty(value = "迭代升级")
    private Integer upProduct;
    @ApiModelProperty(value = "迭代升级中文")
    private String upProductName;
    @ApiModelProperty(value = "可调出串号")
    private Boolean imeiRead;
    @ApiModelProperty(value = "可调出串号中文")
    private String imeiReadName;

    @ApiModelProperty(value = "服务类型中文")
    private String serviceTypeName;

    @ApiModelProperty(value = "理赔类型中文")
    private String claimItemName;

    @ApiModelProperty(value = "失败原因")
    private String failRemark;
    @ApiModelProperty(value = "手机故障")
    private String breakDown;
    @ApiModelProperty(value = "维修城市")
    private String fixCity;
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "理赔人姓名")
    private String fixName;

    @ApiModelProperty(value = "理赔人支付宝号")
    private String fixAlipay;

    @ApiModelProperty(value = "理赔人图片")
    private String fixImage;

    @ApiModelProperty(value = "理赔视频")
    private String fixVedio;

    @ApiModelProperty(value = "联系人电话")
    private String contactMobile;

    @ApiModelProperty(value = "换新机器的IMEI")
    private String newImei;

    public String getOldSkuRetailPrice(){
        if(oldSkuRetailPrice == null) return "";
        return MoneyUtil.fenToYuan(oldSkuRetailPrice);
    }

    public String getDiscountAmount(){
        if(discountAmount == null) return "";
        return MoneyUtil.fenToYuan(discountAmount);
    }

    public String getNewProductSkuRetailPrice(){
        if(newProductSkuRetailPrice == null) return "";
        return MoneyUtil.fenToYuan(newProductSkuRetailPrice);
    }

    public String getSuppleAmount(){
        if(suppleAmount == null) return "";
        return MoneyUtil.fenToYuan(suppleAmount);
    }

    public String getSettleAmount(){
        if(settleAmount == null) return "";
        return MoneyUtil.fenToYuan(settleAmount);
    }

}
