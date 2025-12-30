package com.anyi.sparrow.applet.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description 信用分明细表
 * @author shenbinhong
 * @date 2023-05-16
 */
@Data
@TableName("wm_credit_score_record")
public class WmCreditScoreRecord {


    @TableId(type = IdType.AUTO)
    /**
    * id
    */
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
     * 车牌号
     */
    private String plateNumber;

    /**
     * 积分状态 1、消费积分 2、添加积分
     */
    private Integer state;

    /**
     * 积分内容
     */
    private String content;

    /**
     * 业务类型(1-业务办理充值，2-通行费垫付)
     */
    private Integer type;

    /**
     * 新增/扣除积分
     */
    private Long changeScore;

    /**
     * 原来的积分
     */
    private Long beforeScore;

    /**
     * 计算后的积分
     */
    private Long afterScore;

    /**
     * 变动前暂存分
     */
    private Long beforeTempScore;

    /**
     * 变动后暂存分
     */
    private Long afterTempScore;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}