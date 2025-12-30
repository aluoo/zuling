package com.anyi.sparrow.common.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel
@Data
public class Dicts {

    private Long id;

    @ApiModelProperty("参数")
    private String name;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("描述")
    private String description;

    private BigDecimal sort;

    private Long parentId;


}