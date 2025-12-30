package com.anyi.sparrow.common.utils;

import com.anyi.common.advice.BizError;
import com.anyi.common.advice.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {
    private static final List<String> newCarPlate = Arrays.asList("F", "D");
    private static Pattern compile = Pattern.compile("^[a-zA-Z0-9]$");
    public static void plateLengthValid(String plateNumber) {
        int length = plateNumber.length();
        if (newCarPlate.contains(plateNumber.substring(2, 3))) {
            if (length != 7 && length != 8) {
                throw new BusinessException(BizError.PLATE_LENGTH_ERROR);
            }
        } else {
            if (length != 7) {
                throw new BusinessException(BizError.PLATE_LENGTH_ERROR);
            }
        }
    }
    public static void plateStrValid(String plateNumber) {
        String s = plateNumber.substring(plateNumber.length() - 1);
        Matcher matcher = compile.matcher(s);
        if (!matcher.matches()){
            throw new BusinessException(BizError.PLATE_RULE_ERROR);
        }
    }
    public static void mobileValid(String mobileNumber) {
        if ((StringUtils.isBlank(mobileNumber) || mobileNumber.length() != 11)){
            throw new BusinessException(BizError.MOBILE_LENGTH_ERROR);
        }
    }
}
