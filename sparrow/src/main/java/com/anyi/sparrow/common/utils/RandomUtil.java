package com.anyi.sparrow.common.utils;

import java.util.Random;

public class RandomUtil {
    private RandomUtil(){}

    private static int[] numbers = {0,1,2,3,4,5,6,7,8,9};

    private static String all = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String hexs = "ABCDEF0123456789";

    private static final Random random = new Random();

    public static String randomNumber(Integer length){

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            int i = random.nextInt(10);
            sb.append(i);
        }
        return sb.toString();
    }

    public static String randomStr(int length){
        StringBuilder sb = new StringBuilder();
        while (length > 0){
            sb.append(all.charAt(random.nextInt(all.length())));
            length--;
        }
        return sb.toString();
    }
    public static String randomHex(int length){
        StringBuilder sb = new StringBuilder();
        while (length > 0){
            sb.append(hexs.charAt(random.nextInt(hexs.length())));
            length--;
        }
        return sb.toString();
    }
}
