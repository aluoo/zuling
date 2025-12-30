package com.anyi.sparrow.withdraw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 对公账户提现请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/3/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class InvoiceApplyReq extends NormalApplyReq {

    @ApiModelProperty(value = "expressCompany 物流公司名称")
//    @NotEmpty(message = "物流公司名称不能为空")
    private String expressCompany;

    @ApiModelProperty(value = "物流单号")
//    @NotEmpty(message = "物流单号不能为空")
    private String expressNo;

    @ApiModelProperty(value = "发票类型( 1-电子,2-纸质)")
    @NotNull(message = "发票类型参数不能为空")
    private Integer invoiceType;

    @ApiModelProperty(value = "发票图片地址数组")
    @NotEmpty(message = "发票图片不能为空")
    private List<String> invoiceImgs;


}
