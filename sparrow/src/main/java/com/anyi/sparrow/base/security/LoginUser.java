package com.anyi.sparrow.base.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private Long id;

    private String name;

    private String nickname;

    private Byte status;

    private String mobileNumber;

    private Integer type;

    private Long deptId;

    private String deptCode;

    private Long companyId;

    private Integer companyType;

    private Integer deptType;

    private String openId;
    private String xyOpenId;

    private String avatarUrl;

    private String unionId;

    //注册渠道
    private String channel;

    //等于用户类型1-员工 2-小程序
    private Integer loginUserType;
    /**
     * token过期时间
     */
    private LocalDateTime tokenExpire;

    /**
     * 人员组织层级编码
     */
    private String ancestors;
    /**
     * 人员层级
     */
    private Integer level = -1;

    private boolean temporaryUser = false;

    public boolean isAdmin() {
        return level == 0;
    }
}