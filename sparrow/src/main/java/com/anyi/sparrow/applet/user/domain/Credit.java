package com.anyi.sparrow.applet.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description 积分表
 * @author peng can
 * @date 2022-12-13
 */
@Data
@TableName("wm_credit")
public class Credit {

    /**
    * 积分id
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 用户id
    */
    private Long userId;

    /**
    * etc卡号
    */
    private String etcNo;

    /**
    * 积分值
    */
    private Long score;

    /**
    * 备注
    */
    private String remark;

    /**
     * 车牌号码
     */
    private String plateNumber;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    private LocalDateTime updateTime;

}