package com.anyi.sparrow.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 佣金方案类型(1-推广/发行、2-服务费、3-奖金)
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommissionType {
    PUBLISH(1, "推广佣金"),
    SERVICE(2, "服务费佣金"),
//    BONUS(3, "奖金")
    ;

    int code;
    String name;


    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    private static final Map<Integer, CommissionType> CACHE = new HashMap<Integer, CommissionType>();

    static {
        for (CommissionType val : CommissionType.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static CommissionType parse(Integer code) {
        return CACHE.get(code);
    }


//    public static CommissionType[] getValues(){
//        return  Arrays.stream(CommissionType.values()).filter(item->CommissionType.PUBLISH == item).collect(Collectors.toList()).toArray(new CommissionType[]{});
//    }

}
