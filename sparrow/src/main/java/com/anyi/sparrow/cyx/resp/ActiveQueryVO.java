package com.anyi.sparrow.cyx.resp;

import lombok.Data;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/17
 */
@Data
public class ActiveQueryVO {

    // 开户人名称
    private String ownerName;
    // 开户人手机
    private String driverPhone;
    // 标签号
    private String obuId;
    // 卡号
    private String cardId;
    // 车牌号
    private String plateNum;
    // 车牌颜色
    private String plateColor;
    // 车辆类型
    private String vehicleType;
    // 签约类型
    private String signType;
    // 签约时间
    private String signTime;
    // 激活时间
    private String activeTime;
    // 标识字段
    private String activeText;
}