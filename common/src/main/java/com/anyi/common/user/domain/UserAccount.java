package com.anyi.common.user.domain;

import com.anyi.common.domain.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author pengcan
 * @description 用户信息
 * @date 2022-12-01
 */
@Data
@TableName(value = "wm_user_account")
public class UserAccount extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * wx openid
     */
    private String openId;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String mobileNumber;

    /**
     * 公众号openid
     */
    private String officialOpenId;

    /**
     * 用户unionid
     */
    private String unionId;

    @TableField(exist = false)
    private Long employeeId;
    @TableField(exist = false)
    private Long companyId;
}