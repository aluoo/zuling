package com.anyi.common.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author chenjian
 * @Description
 * @Date 2023/11/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DictMapVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("字典集合")
    private Map<String, String> resultMap;

}