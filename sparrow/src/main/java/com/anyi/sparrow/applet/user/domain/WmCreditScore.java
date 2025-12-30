package com.anyi.sparrow.applet.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description 信用分表
 * @author shenbinhong
 * @date 2023-05-16
 */
@Data
@TableName("wm_credit_score")
public class WmCreditScore {

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
    * 信用分
    */
    private Long score;
    /**
     * 暂存信用分
     */
    private Long tempScore;

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