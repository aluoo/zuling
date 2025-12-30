package com.anyi.common.insurance.response;

import com.anyi.common.insurance.domain.DiOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/2/23
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("理赔项目示例图片")
public class InsuranceDemoImageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("理陪项目名称")
    private String itemName;

    @ApiModelProperty("图片资料")
    @Valid
    private List<DiOptionVO> imageList;

}