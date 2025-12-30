package com.anyi.common.company.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 对公账户提现请求对象
 * </p>
 *
 * @author chenjian
 * @since 2023/11/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class CompanyPackageReq {
    @ApiModelProperty("部门层级编码")
    private Long companyId;
    @ApiModelProperty(value = "第几页", example = "1")
    private Integer page;
    @ApiModelProperty(value = "每页记录数", example = "20")
    private Integer pageSize;


}
