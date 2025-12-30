package com.anyi.common.company.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("company")
public class Company extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long pId;

    private Integer type;

    private String name;

    private String code;

    private Byte status;

    private String contact;

    private String contactMobile;

    private String province;

    private String city;

    private String region;

    private String address;

    private String provinceCode;

    private String cityCode;

    private String regionCode;

    private String frontUrl;
    private String busLicense;

    private String idUrlUp;
    private String idUrlDown;

    private String idCard;

    private String idName;

    private Long employeeId;

    private Long userId;

    private Long aplId;

    private Boolean invoiceAble;

    private Boolean loginAble;

    private Boolean commissionAble;

    private Integer exchangeType;

    private String creator;

    private String updator;

    private Long pDeptId;

    private Integer mbrType;

}