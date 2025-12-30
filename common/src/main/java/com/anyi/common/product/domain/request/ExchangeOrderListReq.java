package com.anyi.common.product.domain.request;

import com.anyi.common.domain.entity.AbstractBaseQueryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/4/7
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("换机晒单列表查询请求对象")
public class ExchangeOrderListReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //门店
    private Long storeCompanyId;

    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    //门店是连锁也要看底下门店
    private List<Long> storeCompanyIds;

    private Long storeEmployeeId;
}