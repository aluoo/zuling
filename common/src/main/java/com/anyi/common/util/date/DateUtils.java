package com.anyi.common.util.date;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 *
 * @author Jianpan
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM","yyyy年MM月dd日HH：mm",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "YYYYMMDD", "yyyyMMddHHmmss", "yyyy/MM/ddHH：mm：ss"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static Date getDateYYYYMMDD(String str) {
        return dateTime(YYYYMMDD, str);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String parseDateToStr(final Date date) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 2023年05月24日 19:47:52
     */
    public static String dateStrByTime(Date date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String str = sdFormat.format(date);
        return str;
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算相差分钟
     */
    public static int differentMinuteByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 60)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static Date[] getPairTime() {

        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());

        // 通过LocalDateTime的 with方法设置某天的最小值和最大值！！
        LocalDateTime minDateTime = localDateTime.with(LocalTime.MIN);
        LocalDateTime maxDateTime = localDateTime.with(LocalTime.MAX);

        // 格式化日期
        Date fromDate = Date.from(minDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date toDate = Date.from(maxDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return new Date[]{fromDate, toDate};
    }

    /**
     * 字符串转成日期 <br/>
     * 格式：datePattern
     *
     * @param dateStr     日期字符串
     * @param datePattern 转换格式
     * @return Date
     */
    public static Date strToDate(String dateStr, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调整年份<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param i
     */
    public static Date adjustYear(Date date, int i) {
        return adjustTime(date, i, 0, 0, 0, 0, 0);
    }

    /**
     * 调整月份<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param i
     */
    public static Date adjustMonth(Date date, int i) {
        return adjustTime(date, 0, i, 0, 0, 0, 0);
    }

    /**
     * 调整天数<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param i
     */
    public static Date adjustDay(Date date, int i) {
        return adjustTime(date, 0, 0, i, 0, 0, 0);
    }

    /**
     * 调整小时<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param i
     */
    public static Date adjustHour(Date date, int i) {
        return adjustTime(date, 0, 0, 0, i, 0, 0);
    }

    /**
     * 调整分数<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param i
     */
    public static Date adjustMinute(Date date, int i) {
        return adjustTime(date, 0, 0, 0, 0, i, 0);
    }

    /**
     * 调整秒数<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param i
     */
    public static Date adjustSecond(Date date, int i) {
        return adjustTime(date, 0, 0, 0, 0, 0, i);
    }

    /**
     * 调整时间<br/>
     * 要调整的基数，正表示加，负表示减
     *
     * @param date
     * @param y    年
     * @param m    月
     * @param d    日
     * @param h    小时
     * @param mm   分钟
     */
    public static Date adjustTime(Date date, int y, int m, int d, int h, int mm, int ss) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.YEAR, y);
        cal.add(Calendar.MONTH, m);
        cal.add(Calendar.DAY_OF_MONTH, d);
        cal.add(Calendar.HOUR_OF_DAY, h);
        cal.add(Calendar.MINUTE, mm);
        cal.add(Calendar.SECOND, ss);
        return new Date(cal.getTimeInMillis());
    }

}
