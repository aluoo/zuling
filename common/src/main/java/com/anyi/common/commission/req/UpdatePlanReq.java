package com.anyi.common.commission.req;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdatePlanReq {

    @NotNull(message = "planId，不能为空")
    private Long planId;

    @NotNull(message = "佣金方案类型Id不能为空")
    private Long bizTypeId;

    @NotNull(message = "方案名称不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$",message ="方案名称不能含特殊字符")
    private String planName;

    @Valid
    private List<Conf> planConf;

    @ApiModelProperty("后端自己传")
    private Long employeeId;


    @Data
    public static class Conf {
        //@NotNull
        private Long confId;

        private Long packageInfoId;

        @DecimalMin(value = "0.00", message = "childDivide：必须大于0")
        private BigDecimal childDivide;

        @ApiModelProperty("方案类型数值的时候传0,范围0-100")
        private BigDecimal childScale;

        public Long getChildDivide() {
            return NumberUtil.mul(childDivide, 100).toBigInteger().longValue();
        }

        public BigDecimal getChildScale() {
            return NumberUtil.div(childScale, 100);
        }

    }
}
