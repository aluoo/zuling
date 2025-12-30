package com.anyi.sparrow.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shenbh
 * @since 2023/3/31
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class ApplyIdDTO implements Serializable {

    @ApiModelProperty(value = "提交申请ID", dataType = "Long", example = "60035")
    private Long applyId;


}
