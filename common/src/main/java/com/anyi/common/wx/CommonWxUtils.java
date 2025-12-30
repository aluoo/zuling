package com.anyi.common.wx;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonWxUtils {
    public static final Integer HUNDRED = 100;
    private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String UNION = "UN";
    private static final String ANYI = "AY";

    public static final String HYPHEN = "-";
    public static final String TEST_HYPHEN = "T";
    // 发行方编
    public static final String PUBLIC_CODE_A = "A";


    public static String createOrderNo() {
        return ANYI + new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date())
                + RandomUtil.randomNumbers(14);
    }

    public static String createUnionTradeNo() {
        return UNION + createOrderNo();
    }

    public static boolean isUnionTradeNo(String unionTradeNo) {
        return unionTradeNo.startsWith(UNION);
    }

    public static Date toDate(String dateStr) {
        try {
            return new SimpleDateFormat(YYYYMMDDHHMMSS).parse(dateStr);
        } catch (ParseException e) {
            throw new BaseException(-1, e.getMessage());
        }
    }

    public static Date toDateV3(String dateStr) {
        /*try {
            return new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            throw new AlertException(e.getMessage());
        }*/
        try {
            return DateUtil.parse(dateStr);
        } catch (Exception e) {
            throw new BaseException(-1, e.getMessage());
        }
    }

    public static String getTimeExpireV3(Date begin, int offsetMinute) {
        // yyyy-MM-DDTHH:mm:ss+TIMEZONE
        // 2018-06-08T10:34:56+08:00
        DateTime offset = DateUtil.offsetMinute(begin, offsetMinute);
        return DateUtil.format(offset, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
    }

    public static String toStr(final Date date) {
        return new SimpleDateFormat(YYYYMMDDHHMMSS).format(date);
    }

    public static String createOrderNo(Long snowflakeId, int serviceFee, boolean isProduction) {
        return createOrderNo(snowflakeId, serviceFee, null, null, isProduction);
    }

    public static String createOrderNo(Long snowflakeId, int serviceFee, String dateStr, Boolean duringPackageDiscount, boolean isProduction) {
        dateStr = StrUtil.isNotBlank(dateStr) ? dateStr : DateUtil.today();
        String FORMAT_DATE  = DateUtil.format(DateUtil.parse(dateStr), DatePattern.PURE_DATE_PATTERN);
        //服务费标识（0为收取、1为不收取）
        //String feeFlag = serviceFee > 0 ? "0" : "1";
        String feeFlag;
        if (duringPackageDiscount == null) {
            feeFlag = serviceFee > 0 ? "0" : "1";
        } else {
            feeFlag = duringPackageDiscount ? "1" : "0";
        }
        String prefix = ANYI + PUBLIC_CODE_A + FORMAT_DATE + feeFlag;

        prefix = isProduction ? prefix + HYPHEN : prefix + TEST_HYPHEN;

        String no = String.valueOf(snowflakeId);
        if (no.length() > 19) {
            no = StrUtil.sub(no, 0, 19);
        }
        return prefix + no;
    }

    public static String fenToYuan(int amount) {
        return new BigDecimal(amount).divide(new BigDecimal(HUNDRED), 2, RoundingMode.UNNECESSARY).toString();
    }
}