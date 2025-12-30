package com.anyi.common.employee.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description 用户登陆表
 * @author pengcan
 * @date 2022-12-03
 */
@Data
@TableName("wm_user_login")
public class UserLogin {


    /**
    * 用户id
    */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
    * 登录时间
    */
    private LocalDateTime loginTime;

    /**
    * token
    */
    private String token;

    /**
    * token过期时间
    */
    private LocalDateTime tokenExpire;

}