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
public class MailAdressDTO implements Serializable {

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("收件人")
    private String name;

    @ApiModelProperty("联系方式")
    private String phone;


}
