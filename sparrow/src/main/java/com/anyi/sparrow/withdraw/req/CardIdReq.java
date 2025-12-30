package com.anyi.sparrow.withdraw.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author shenbh
 * @since 2023/3/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class CardIdReq {

    @ApiModelProperty(value = "卡id")
    @NotNull(message = "cardId不能为空")
    private Long cardId;

}
