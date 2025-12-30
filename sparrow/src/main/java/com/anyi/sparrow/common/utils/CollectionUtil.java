package com.anyi.sparrow.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class CollectionUtil {

    public static String mapToSortStr(JSONObject param, Set<String> noSign) {
        List<String> keys = new ArrayList<>(param.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        keys.forEach(key -> {
            if (noSign == null || !noSign.contains(key)) {
                sb.append("&").append(key).append("=").append(param.get(key));
            }
        });
        return sb.substring(1);
    }



    public static String mapToSortStr(Map<String, String> param) {
        List<String> keys = new ArrayList<>(param.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        keys.forEach(key -> sb.append("&").append(key).append("=").append(param.get(key)));
        return sb.substring(1);
    }
}
