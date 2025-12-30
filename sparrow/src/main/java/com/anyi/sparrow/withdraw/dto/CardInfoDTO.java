package com.anyi.sparrow.withdraw.dto;

import com.anyi.sparrow.withdraw.constant.WithdrawCardIllegalTypeEnum;
import com.anyi.sparrow.withdraw.constant.WithdrawCardStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author shenbh
 * @since 2023-03-30
 */
@Getter
@Setter
@ApiModel(value = "WithdrawEmployeeCard对象", description = "账户提现方式绑定")
public class CardInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long cardId;


    @ApiModelProperty("类型(1-银行卡、2-支付宝、3-对公账户)")
    @JsonIgnore
    private Integer type;

    @ApiModelProperty("银行名称")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNo;

    @ApiModelProperty("姓名")
    private String ownerName;

    @ApiModelProperty("身份证号")
    private String idCard;


    @JsonIgnore
    private Integer status;


    @ApiModelProperty("公司名称(对公)")
    private String companyName;

    @ApiModelProperty("公司税号(对公)")
    private String companyTaxNo;

    @ApiModelProperty("地址(对公)")
    private String address;

    @ApiModelProperty("是否异常")
    private Boolean illegal;

    @JsonIgnore
    private String illegalTypes;

    @ApiModelProperty("异常信息")
    private String illegalMsg;


    @ApiModelProperty("累计提现信息")
    private String accWithdraw;

    public Boolean getIllegal() {
        return WithdrawCardStatusEnum.abnormal.getType() == this.status;
    }

    public String getIllegalMsg() {

        if (StringUtils.isNotEmpty(illegalMsg)) {
            return illegalMsg;
        }

        if (getIllegal()) {
            if (StringUtils.isNotEmpty(illegalTypes)) {

                StringBuilder sb = new StringBuilder();
                String[] types = illegalTypes.split(",");
                for (int i = 0; i < types.length; i++) {
                    WithdrawCardIllegalTypeEnum illegalTypeEnum = null;
                    try {
                        illegalTypeEnum = WithdrawCardIllegalTypeEnum.valueOf(types[0]);
                    } catch (Exception e) {

                    }

                    if (illegalTypeEnum != null) {
                        if (sb.length() > 0) {
                            sb.append("或");
                        }
                        sb.append(illegalTypeEnum.getMessage());
                    }

                }

                return sb.toString();

            }
        }

        return null;
    }
}
