package com.anyi.sparrow.account.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("employee_real_name_verification")
@ApiModel(value = "EmployeeRealNameVerification对象", description = "员工账户实名认证表")
public class EmployeeRealNameVerification extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    @ApiModelProperty("是否删除")
    private Boolean deleted;
    @ApiModelProperty("员工ID")
    private Long employeeId;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("身份证号")
    private String idCard;
    @ApiModelProperty("身份证正面")
    private String idUrlUp;
    @ApiModelProperty("身份证背面")
    private String idUrlDown;
    @ApiModelProperty("接口返回日志")
    private String remoteResp;
}