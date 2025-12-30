package com.anyi.sparrow.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class FileUploadRes {
    @ApiModelProperty("文件地址")
    private String fileUrl;
}
