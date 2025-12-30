package com.anyi.sparrow.common.utils;


import java.nio.ByteBuffer;

public class HexUtil {
    /**
     * 字符串转化成为16进制字符串
     *
     * @param s
     * @return
     */
    public static String strTo16(String s, String charset) {
        // 处理特殊字符
        s = s.replaceAll("\\n", "");
        s = s.replaceAll("\\r", "");
        StringBuilder st = new StringBuilder();
        try {
            byte[] by = s.getBytes(charset);
            for (byte b : by) {
                String strs = Integer.toHexString(b);
                if (strs.length() > 2) {
                    strs = strs.substring(strs.length() - 2);
                }
                st.append(strs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st.toString().toUpperCase();
    }

    public static final String strTo16(String s) {
        return strTo16(s, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        String s = "0000B0ACBACFC2F3CCE1BDAD3FD2C1CBB9C2EDD2C1C1A6363533303232313937303031303132303332000000000000000000000000000000";
//        String plate = s.substring(0, 24);
        System.out.println(hexStringToString(s));
        System.out.println(strTo16("伊斯马伊力", "gb2312"));
        String str = "3F";
        System.out.println(hexStringToString(str));
    }

    public static String numToHex16(int b) {
        return String.format("%02x", b);//2表示需要两个16进行数
    }

    public static byte[] hexStr2Byte(String hex) {
        ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            bf.put(b);
        }
        return bf.array();
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gb2312");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }
}
