package com.anyi.sparrow.mobileStat.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyStatReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("人员层级编码,第一层级不用传")
    @NotBlank(message = "人员层级编码不能为空")
    private String ancestors;

    @ApiModelProperty("部门管理员Level,第一层级不用传")
    @NotNull(message = "Level不能为空")
    private Integer level;

    @ApiModelProperty("开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("直营门店专用邀请人员ID,直营统计需要必传")
    private Long aplId;
}
