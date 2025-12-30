package com.anyi.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Date 2023/5/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("verify_code")
public class VerifyCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String mobileNumber;

    private String code;

    private Byte status;

    private Date expireTime;

    private String biz;

    private Date createTime;
}