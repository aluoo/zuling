package com.anyi.sparrow.assist.notice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("公告信息返回对象")
public class NoticeVo {

    @ApiModelProperty("公告内容")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
