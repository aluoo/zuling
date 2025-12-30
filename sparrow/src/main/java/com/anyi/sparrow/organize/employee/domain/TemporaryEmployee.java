package com.anyi.sparrow.organize.employee.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("temporary_employee")
public class TemporaryEmployee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long companyId;

    private Integer companyType;

    private Long deptId;

    private Integer deptType;

    private Integer type;

    private String name;

    private String deptCode;

    private Byte status;

    private String mobileNumber;

    private String headUrl;

    private String password;

    private Date createTime;

    private Date updateTime;
}