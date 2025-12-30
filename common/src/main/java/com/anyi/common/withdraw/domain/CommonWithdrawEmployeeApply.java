package com.anyi.common.withdraw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 提现申请表
 * </p>
 *
 * @author shenbh
 * @since 2023-03-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("withdraw_employee_apply")
@ApiModel(value = "WithdrawEmployeeApply对象", description = "提现申请表")
public class CommonWithdrawEmployeeApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("提现单号")
    private String applyNo;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("提现金额(分)")
    private Long amount;

    @ApiModelProperty("代扣税额")
    private Long taxAmount;

    @ApiModelProperty("到手金额")
    private Long inAmount;

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

    @ApiModelProperty("是否对公(1-是,0-否)")
    private Boolean toPublic;

    @ApiModelProperty("公司名称(对公)")
    private String companyName;

    @ApiModelProperty("公司税号(对公)")
    private String companyTaxNo;

    @ApiModelProperty("地址(对公)")
    private String address;

    @ApiModelProperty("是否需要发票")
    private Boolean invoiceFlag;

    @ApiModelProperty("发票类型(电子纸质)")
    private Integer invoiceType;

    @ApiModelProperty("发票图片url(多张)")
    private String invoiceImgs;

    @ApiModelProperty("发票上传时间")
    private LocalDateTime invoiceUploadTime;

    @ApiModelProperty("物流公司")
    private String expressCompany;

    @ApiModelProperty("物流单号")
    private String expressNo;

    @ApiModelProperty("发票审核状态")
    private Integer invoiceStatus;

    @ApiModelProperty("发票审核时间")
    private LocalDateTime invoiceCheckTime;

    @ApiModelProperty("申请状态")
    private Integer status;
    /**
     * 备注
     */
    @JsonIgnore
    private String remark;

    @ApiModelProperty("平台审核类型(0-后台、1-渠道子后台)")
    private Boolean toPlatform;

    @ApiModelProperty("归属审核人id(平台审核类型为渠道子后台时有值)")
    private Long toEmployeeId;

    @ApiModelProperty("组织层级编码")
    private String ancestors;

    /**
     * 状态快照
     */
    @JsonIgnore
    private String stateSnapshot;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}