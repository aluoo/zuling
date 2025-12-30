package com.anyi.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>
 * 金额工具类
 * </p>
 *
 * @author shenbh
 * @since 2023.03.13
 */
public class MoneyUtil {
    public static final Integer HUNDRED = 100;
    public static final String ZERO_YUAN = "0.00";

    /**
     * 将Long型金额(单位分) 转换为 保留两位小数的 金额字符串 eg: 1500 L ->  "15.00"
     * @param balance Long型金额(单位分)
     * @return
     */
    public static String convert(Long balance) {
        if (balance == null) {
            return ZERO_YUAN;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        Double doubleBalance = Double.valueOf(balance) / 100;
        String format = df.format(doubleBalance);
        return format;
    }

    public static String fenToYuan(Integer amount) {
        if (amount == null) {
            return ZERO_YUAN;
        }
        return new BigDecimal(amount).divide(new BigDecimal(HUNDRED), 2, RoundingMode.UNNECESSARY).toString();
    }
}