package com.anyi.sparrow.withdraw.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 账户提现方式绑定
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Getter
@Setter
@TableName("withdraw_employee_card")
@ApiModel(value = "WithdrawEmployeeCard对象", description = "账户提现方式绑定")
public class WithdrawEmployeeCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("类型(1-银行卡、2-支付宝、3-对公账户)")
    private Integer type;

    @ApiModelProperty("银行名称")
    private String accountName;

    @ApiModelProperty("账号")
    private String accountNo;

    @ApiModelProperty("姓名")
    private String ownerName;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("是否设为默认(0-否,1-默认)")
    private Boolean defaultFlag;

    @ApiModelProperty("最近使用时间")
    private LocalDateTime latestTime;

    @ApiModelProperty("公司名称(对公)")
    private String companyName;

    @ApiModelProperty("公司税号(对公)")
    private String companyTaxNo;

    @ApiModelProperty("地址(对公)")
    private String address;

    @ApiModelProperty("状态(1-正常、0-待审核、2-异常)")
    private Integer status;

    @ApiModelProperty("异常类型")
    private String illegalTypes;

    @ApiModelProperty("审核通过时间")
    private LocalDateTime passTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
