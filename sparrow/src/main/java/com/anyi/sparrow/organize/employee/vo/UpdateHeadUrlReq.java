package com.anyi.sparrow.organize.employee.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/21
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateHeadUrlReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "头像不能为空")
    private String headUrl;
}