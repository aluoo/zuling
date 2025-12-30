package com.anyi.common.commission.req;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PlanReq {

    @NotBlank(message = "佣金方案名称不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$",message ="方案名称不能含特殊字符")
    private String planName;

    @NotNull(message = "佣金方案类型Id不能为空")
    private Long bizTypeId;

    @NotEmpty(message = "planConf，不能为空")
    private List<Conf> planConf;

    //@NotEmpty(message = "members，不能为空")
    private List<Long> members;

    @ApiModelProperty("后端自己传")
    private Long employeeId;

    @Data
    public static class Conf {
        private long confId;

        private long packageInfoId;

        @ApiModelProperty("方案类型数值的时候传0,范围0-100")
        private BigDecimal childScale;
        @ApiModelProperty("方案类型比例的时候传0,范围0-100")
        private BigDecimal childDivide;

        public long getChildDivide() {
            return NumberUtil.mul(childDivide, 100).toBigInteger().longValue();
        }

        public BigDecimal getChildScale() {
            return NumberUtil.div(childScale, 100);
        }
    }
}
