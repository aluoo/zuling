package com.anyi.common.account.req;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资金明细列表接口请求对象
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@Data
@ApiModel
public class PartnerAccountLogReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 管理员底下所有员工
     */
    private List<Long> employeeIds;

    /**
     * 精确看某个员工
     */
    private String mobile;
    /**
     * 精确看某个员工ID
     */
    private Long employeeId;

    @ApiModelProperty("渠道码,平台方提供")
    private String channelCode;


}
