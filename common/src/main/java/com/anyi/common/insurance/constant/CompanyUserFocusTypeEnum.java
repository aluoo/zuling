package com.anyi.common.insurance.constant;

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
public enum CompanyUserFocusTypeEnum {
    /**
     * 门店充值
     */
    recharge("充值", 40010, 40015),
    /**
     * 投保付款
     */
    insurancePay("投保付款", 30010, 30015),
    /**
     * 投保退款
     */
    insuranceRefund("投保退款", 30020, 30025),
    ;
    private String name;

    private int changeDetailTypeStart;
    private int changeDetailTypeEnd;


    public static List<Map<String, String>> getTypesForUserSelect() {
        CompanyUserFocusTypeEnum[] ary = CompanyUserFocusTypeEnum.values();

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

    public static CompanyUserFocusTypeEnum getByCode(String typeCode) {
        CompanyUserFocusTypeEnum[] ary = CompanyUserFocusTypeEnum.values();
        for (int num = 0; num < ary.length; num++) {
            if (ary[num].name().equals(typeCode)) {
                return ary[num];
            }
        }

        return null;
    }

    public static CompanyUserFocusTypeEnum findUserFocusTypeBy(int changeDetailType){
        CompanyUserFocusTypeEnum[] ary = CompanyUserFocusTypeEnum.values();
        for (int num = 0; num < ary.length; num++) {
            if (ary[num].getChangeDetailTypeStart()<=changeDetailType && ary[num].getChangeDetailTypeEnd()>=changeDetailType ) {
                return ary[num];
            }
        }
        return null;
    }


}
