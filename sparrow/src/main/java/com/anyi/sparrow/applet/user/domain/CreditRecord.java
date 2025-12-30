package com.anyi.sparrow.applet.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description 积分明细表
 * @author peng can
 * @date 2022-12-13
 */
@Data
@TableName("wm_credit_record")
public class CreditRecord {


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
     * 积分状态 1、消费积分 2、添加积分
     */
    private Integer state;

    /**
     * 积分内容
     */
    private String content;

    /**
     * 新增/扣除积分
     */
    private Long tempScore;

    /**
     * 原来的积分
     */
    private Long originalScore;

    /**
     * 计算后的积分
     */
    private Long resultScore;

    /**
     * 备注
     */
    private String remark;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 业务类型(1-业务办理充值，2-通行费垫付)
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}