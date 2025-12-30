package com.anyi.common.insurance.response;

import com.anyi.common.insurance.domain.DiOption;
import com.anyi.common.util.MoneyUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class DiFixInsuranceDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("投保单ID")
    private Long orderId;

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

    public String getOldSkuRetailPrice(){
        if(oldSkuRetailPrice == null) return "";
        return MoneyUtil.fenToYuan(oldSkuRetailPrice);
    }

    public String getDiscountAmount(){
        if(discountAmount == null) return "";
        return MoneyUtil.fenToYuan(discountAmount);
    }

}
