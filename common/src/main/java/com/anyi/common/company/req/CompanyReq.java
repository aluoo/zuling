package com.anyi.common.company.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class CompanyReq {
    @ApiModelProperty("人员层级编码")
    private String ancestors;
    @ApiModelProperty(value = "第几页", example = "1")
    private Integer page = 1;
    @ApiModelProperty(value = "每页记录数", example = "20")
    private Integer pageSize = 10;
    @ApiModelProperty("1-待审核， 2-正常， 3-审核失败， 4-注销，5-下线")
    private Integer status;
    @ApiModelProperty("省份编码")
    private String provinceCode;
    @ApiModelProperty("城市编码")
    private String cityCode;
    @ApiModelProperty("地区编码")
    private String regionCode;
    @ApiModelProperty("后端自用")
    private List<Long> empIds;
    @ApiModelProperty("后端自用")
    private List<Long> parentIds;
    @ApiModelProperty("后端自用,直营邀请人ID")
    private Long aplId;
    @ApiModelProperty("2单店3连锁4回收商")
    private Integer type;


}
