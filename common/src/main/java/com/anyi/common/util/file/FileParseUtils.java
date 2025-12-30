package com.anyi.common.util.file;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/8/21
 */
public class FileParseUtils {

    public static String removePrefix0(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            if ('0' == sb.charAt(i)) {
                sb.deleteCharAt(i);
                i--;
            } else {
                break;
            }
        }
        return sb.toString();
    }
}