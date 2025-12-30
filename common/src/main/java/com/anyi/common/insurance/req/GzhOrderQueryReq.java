package com.anyi.common.insurance.req;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/27
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class GzhOrderQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("IMEI")
    private String imei;
    @ApiModelProperty("证件号码")
    private String idCard;

    @ApiModelProperty("APP端门店ID")
    private Long companyId;

    @ApiModelProperty("APP端门店管理")
    private Boolean manage = false;

    @ApiModelProperty("APP端门店员工ID")
    private Long storeEmployeeId;

    @ApiModelProperty("0报险待审核  1理赔资料审核 2上传理赔资料 3修改报险资料 4待理赔 5修改理赔资料 6已理赔 7订单取消")
    private Integer status;

    @ApiModelProperty("投保单ID")
    private Long insuranceOrderId;

}