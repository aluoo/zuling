package com.anyi.sparrow.assist.projects.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SysProjectsRps {

    private Integer projectId;

    private Integer projectCode;

    private String projectName;

    private Integer device;

    private Integer buildCode;

    private String versionCode;

    private Date onlineTime;

    private Integer status;

    private String downloadUrl;

    @ApiModelProperty("是否强制更新（1是2否）")
    private Integer forcedUpdating;

    private Date creatTime;

    private String creator;

    private String readme;

}
