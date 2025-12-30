package com.anyi.common.commission.dto.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class PersonStatDTO implements Serializable {

    /**
     * 待结算总计
     */
    @ApiModelProperty("待结算总计")
    private String accWaitSettile;

    /**
     * 我的个人收益- 待结算
     */
    @ApiModelProperty("我的个人收益- 待结算")
    private String personWaitSettile;
    /**
     * 我的个人收益- 累计收益
     */
    @ApiModelProperty("我的个人收益- 累计收益")
    private String personAccSettle;
    /**
     * 我的团队收益- 待结算
     */
    @ApiModelProperty("我的团队收益- 待结算")
    private String personTeamWaitSettile;
    /**
     * 我的团队收益- 累计收益
     */
    @ApiModelProperty("我的团队收益- 累计收益")
    private String personTeamAccSettle;


}
