package com.anyi.sparrow.withdraw.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.anyi.common.util.MoneyUtil;
import com.anyi.sparrow.withdraw.constant.WithdrawCardTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 可提现首页页面接口
 *
 * @author shenbh
 * @since 2023/3/23
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawIndexDTO implements Serializable {

    @ApiModelProperty(value = "实名认证状态 true已认证，直接进入下一步提现 false未认证，弹窗提示进入实名认证")
    private Boolean realNameVerified;

    @ApiModelProperty(value = "可提现余额", dataType = "string", example = "20.00")
    private Long ableBalance;
    @ApiModelProperty(value = "单次最小提现金额,单位分")
    private Long limitMin;
    @ApiModelProperty(value = "单次最大提现金额,单位分")
    private Long limitMax;
    @ApiModelProperty(value = "每日单卡限制提现次数")
    private Integer limitTimes;

    @ApiModelProperty(value = "银行卡")
    private List<AvaliableCardDTO> cardType1;
    @ApiModelProperty(value = "支付宝账号")
    private List<AvaliableCardDTO> cardType2;
    @ApiModelProperty(value = "对公账户")
    private List<AvaliableCardDTO> cardType3;


    public String getAbleBalance() {
        return MoneyUtil.convert(this.ableBalance);
    }

    public void setCardType1(List<AvaliableCardDTO> cardType1) {
        this.cardType1 = desensitize(cardType1);
    }

    public void setCardType2(List<AvaliableCardDTO> cardType2) {
        this.cardType2 = desensitize(cardType2);
    }

    public void setCardType3(List<AvaliableCardDTO> cardType3) {
        this.cardType3 = desensitize(cardType3);
    }

    public static List<AvaliableCardDTO> desensitize(List<AvaliableCardDTO> cardType) {
        if (CollectionUtil.isNotEmpty(cardType)) {
            for (AvaliableCardDTO cardInfoDTO : cardType) {
                if (cardInfoDTO.getType() == WithdrawCardTypeEnum.BANK_CARD.getType() || cardInfoDTO.getType() == WithdrawCardTypeEnum.PUBLIC_ACCOUNT.getType()) {
                    cardInfoDTO.setAccountNo(subBankCardNo(DesensitizedUtil.bankCard(cardInfoDTO.getAccountNo())));
                } else {
                    if (StringUtils.contains("@", cardInfoDTO.getAccountNo())) {
                        cardInfoDTO.setAccountNo(DesensitizedUtil.email(cardInfoDTO.getAccountNo()));
                    } else {
                        cardInfoDTO.setAccountNo(DesensitizedUtil.mobilePhone(cardInfoDTO.getAccountNo()));
                    }
                }
            }
        }
        return cardType;

    }

    public static String subBankCardNo(String bankCard) {
        if (bankCard.length() <= 8) {
            return bankCard;
        }
        return bankCard.substring(bankCard.length() - 8, bankCard.length());
    }

}