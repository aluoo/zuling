package com.anyi.common.commission.dto.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class TeamWaitSettlesDTO implements Serializable {

    /**
     * 待结算总计
     */
    @ApiModelProperty("待结算总计")
    private String accWaitSettile;

    /**
     * 待结算记录
     */
    @ApiModelProperty("待结算记录")
    private List<TeamWaitSettlesDetailDTO> list = new ArrayList<>();


}
