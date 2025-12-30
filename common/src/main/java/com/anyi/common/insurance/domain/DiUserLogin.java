package com.anyi.common.insurance.domain;

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
@TableName("di_user_login")
public class DiUserLogin implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Date loginTime;

    private String token;

    private Date tokenExpire;
}