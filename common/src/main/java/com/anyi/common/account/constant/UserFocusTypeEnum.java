package com.anyi.common.account.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关注的资金变动类型
 * </p>
 *
 * @author shenbh
 * @since 2023/3/24
 */
@Getter
@AllArgsConstructor
public enum UserFocusTypeEnum {
    withdraw("提现", 10010, 10012),
    /**
     * 平台服务费
     */
    mobile_service("交易服务费", 20010, 20015),
    /**
     * APP拉新
     */
    app_new("APP拉新", 30010, 30015),
    /**
     * 回收商充值
     */
    recharge("充值", 40010, 40015),
    /**
     * 回收商手机回收
     */
    mobile_collect("手机回收", 40020, 40025),
    /**
     * 门店手机成交
     */
    mobile_complete("手机成交", 50010, 50015),
    ;
    private String name;

    private int changeDetailTypeStart;
    private int changeDetailTypeEnd;


    public static List<Map<String, String>> getTypesForUserSelect() {
        UserFocusTypeEnum[] ary = UserFocusTypeEnum.values();

        List<Map<String, String>> result = new ArrayList<>();


        for (int num = 0; num < ary.length; num++) {
            Map<String, String> map = new HashMap<String, String>();
            String key = ary[num].name();
            map.put("typeName", ary[num].getName());
            map.put("typeCode", key);

            result.add(map);
        }

        return result;

    }

    public static UserFocusTypeEnum getByCode(String typeCode) {
        UserFocusTypeEnum[] ary = UserFocusTypeEnum.values();
        for (int num = 0; num < ary.length; num++) {
            if (ary[num].name().equals(typeCode)) {
                return ary[num];
            }
        }

        return null;
    }

    public static UserFocusTypeEnum findUserFocusTypeBy(int changeDetailType){
        UserFocusTypeEnum[] ary = UserFocusTypeEnum.values();
        for (int num = 0; num < ary.length; num++) {
            if (ary[num].getChangeDetailTypeStart()<=changeDetailType && ary[num].getChangeDetailTypeEnd()>=changeDetailType ) {
                return ary[num];
            }
        }
        return null;
    }


}