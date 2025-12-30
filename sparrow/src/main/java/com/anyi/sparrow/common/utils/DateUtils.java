package com.anyi.sparrow.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理
 */
public class DateUtils {
    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_PATTERN_YYYY_MM_DD_ZERO = "yyyy-MM-dd 00:00:00";

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 计算距离现在多久，非精确
     *
     * @param date
     * @return
     */
    public static String getTimeBefore(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        } else if (hour > 0) {
            r += hour + "小时";
        } else if (min > 0) {
            r += min + "分";
        } else if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }

    /**
     * 计算距离现在多久，精确
     *
     * @param date
     * @return
     */
    public static String getTimeBeforeAccurate(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        }
        if (hour > 0) {
            r += hour + "小时";
        }
        if (min > 0) {
            r += min + "分";
        }
        if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }

    /**
     * 获取当前系统日期字符串<br/>
     * 格式：yyyy-MM-dd
     * @author cmulin
     */
    public static String getCurDateStr() {
        return getDateStr(new Date());
    }

    /**
     * 获取当前系统日期字符串<br/>
     * 格式：yyyyMMdd
     * @author cmulin
     */
    public static String getCurDateStr2() {
        return getDateStr2(new Date());
    }

    /**
     * 获取当前系统日期时间字符串<br/>
     * 格式：yyyy-MM-dd HH:mm:ss
     * @author cmulin
     */
    public static String getCurDateTimeStr() {
        return getDateTimeStr(new Date());
    }

    /**
     * 获取当前系统日期时间字符串<br/>
     * 格式：yyyyMMddHHmmss
     * @author cmulin
     */
    public static String getCurDateTimeStr2() {
        return getDateTimeStr2(new Date());
    }

    /**
     * 获取当前系统时间(仅时间) 如12:20:30.时间为24小时制,分钟和秒数都是两位，不足补0<br/>
     * 格式：HH:mm:ss
     * @author cmulin
     * @date Mar 20, 2015 5:04:39 PM
     */
    public static String getCurTimeStr(){
        return getTimeStr(new Date());
    }

    /**
     * 获取当前系统年份<br/>
     * 格式：yyyy，如：2015
     * @author cmulin
     * @date Mar 21, 2015 10:02:37 AM
     */
    public static String getCurYearStr(){
        return getYearStr(new Date());
    }

    /**
     * 获取当前系统月份<br/>
     * 格式：M
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getCurMonthStr(){
        return getMonthStr(new Date());
    }

    /**
     * 获取当前系统一个月中的第几天<br/>
     * 格式：d，如当前是4月1日将返回'1'
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getCurDayStr(){
        return getDayStr(new Date());
    }

    /**
     * 获取当前系统24制小时数<br/>
     * 格式：HH，如当前是22:30:34返回22
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getCurHourStr(){
        return getHourStr(new Date());
    }

    /**
     * 获取当前系统分钟数<br/>
     * 格式：m
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getCurMinuteStr(){
        return getMinuteStr(new Date());
    }

    /**
     * 获取当前系统星期数<br/>
     * 如星期二将返回"星期二"
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getCurWeekStr(){
        return getWeekStr(new Date());
    }

    /**
     * 获取指定日期的日期字符串<br/>
     * 格式：yyyy-MM-dd
     * @param date 日期
     * @author cmulin
     */
    public static String getDateStr(Date date){
        return dateToStr(date,"yyyy-MM-dd");
    }

    /**
     * 获取指定日期的日期字符串<br/>
     * 格式：yyyyMMdd
     * @param date 日期
     * @author cmulin
     */
    public static String getDateStr2(Date date) {
        return dateToStr(date,"yyyyMMdd");
    }

    /**
     * 获取指定日期的时间字符串<br/>
     * 格式：yyyy-MM-dd HH:mm:ss
     * @param date
     * @author cmulin
     */
    public static String getDateTimeStr(Date date) {
        return dateToStr(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取指定日期的日期时间字符串<br/>
     * 格式：yyyyMMddHHmmss
     * @param date
     * @author cmulin
     */
    public static String getDateTimeStr2(Date date) {
        return dateToStr(date,"yyyyMMddHHmmss");
    }

    /**
     * 获取指定日期的时间(仅时间) 如12:20:30.时间为24小时制,分钟和秒数都是两位，不足补0<br/>
     * 格式：HH:mm:ss
     * @param date 日期
     * @author cmulin
     * @date Mar 20, 2015 5:04:39 PM
     */
    public static String getTimeStr(Date date){
        return dateToStr(date, "HH:mm:ss");
    }

    /**
     * 获取指定日期的年份<br/>
     * 格式：yyyy，如：2015
     * @author cmulin
     * @date Mar 21, 2015 10:02:37 AM
     */
    public static String getYearStr(Date date){
        return dateToStr(date, "yyyy");
    }

    /**
     * 获取指定日期的月份<br/>
     * 格式：M
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getMonthStr(Date date){
        return dateToStr(date, "M");
    }

    /**
     * 获取指定日期的一个月中的第几天<br/>
     * 格式：d，如当前是4月1日将返回'1'
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getDayStr(Date date){
        return dateToStr(date, "d");
    }

    /**
     * 获取指定日期的24制小时数<br/>
     * 格式：HH，如当前是22:30:34返回22
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getHourStr(Date date){
        return dateToStr(date, "HH");
    }

    /**
     * 获取指定日期的分钟数<br/>
     * 格式：m
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getMinuteStr(Date date){
        return dateToStr(date, "m");
    }

    /**
     * 获取指定日期的星期数<br/>
     * 如星期二将返回"星期二"
     * @author cmulin
     * @date Mar 21, 2015 11:51:38 AM
     */
    public static String getWeekStr(Date date){
        return dateToStr(date, "E");
    }

    /**
     * 日期转换成字符串<br/>
     * 格式：datePattern
     * @param date 日期
     * @param datePattern 格式
     * @author cmulin
     * @date Mar 20, 2015 4:42:35 PM
     */
    public static String dateToStr(Date date,String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        return sdf.format(date);
    }

    /**
     * 字符串转换成日期<br/>
     * 格式：yyyy-MM-dd
     * @param dateStr 日期字符串
     */
    public static Date getDateByDateStr(String dateStr){
        return strToDate(dateStr, "yyyy-MM-dd");
    }

    /**
     * 字符串转换成日期<br/>
     * 格式：yyyy-MM-dd HH:mm:ss
     * @param dateTimeStr 日期字符串
     */
    public static Date getDateByDateTimeStr(String dateTimeStr)  {
        return strToDate(dateTimeStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转成日期 <br/>
     * 格式：datePattern
     * @param dateStr 日期字符串
     * @param datePattern 转换格式
     * @return Date
     */
    public static Date strToDate(String dateStr,String datePattern){
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        if(dateStr == null || "".equals(dateStr)){
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
     * 获取指定年月最大的天数
     * @param year 年
     * @param month 月
     */
    public static int getDayCountByMonth (String year,String month){
        return getDayCountByMonth (IntUtil.convertToInt(year), IntUtil.convertToInt(month));
    }
    /**
     * 获取指定年月的总天数
     * @param year 年
     * @param month 月
     */
    public static int getDayCountByMonth (int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, month-1);//Java月份才0开始算
        /**获取当前年月最大的天数*/
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取星期几
     * @param date
     * @return Integer
     */
    public static int getWeekInt(Date date){
        int xq = 0;
        if (date != null) {
            Calendar tmpCalendar = Calendar.getInstance();
            tmpCalendar.setTime(date);
            xq = tmpCalendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (xq == 0) {
                xq = 7;
            }
        }
        return xq;
    }

    /**
     * 调整年份<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param i
     */
    public static Date adjustYear(Date date,int i) {
        return adjustTime(date,i, 0, 0, 0, 0,0);
    }

    /**
     * 调整月份<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param i
     */
    public static Date adjustMonth(Date date,int i) {
        return adjustTime(date,0, i, 0, 0, 0,0);
    }

    /**
     * 调整天数<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param i
     */
    public static Date adjustDay(Date date,int i) {
        return adjustTime(date,0, 0, i, 0, 0,0);
    }

    /**
     * 调整小时<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param i
     */
    public static Date adjustHour(Date date,int i) {
        return adjustTime(date,0, 0, 0, i, 0,0);
    }

    /**
     * 调整分数<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param i
     */
    public static Date adjustMinute(Date date,int i) {
        return adjustTime(date,0, 0, 0, 0, i,0);
    }

    /**
     * 调整秒数<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param i
     */
    public static Date adjustSecond(Date date,int i) {
        return adjustTime(date,0, 0, 0, 0, 0, i);
    }

    /**
     * 调整时间<br/>
     * 要调整的基数，正表示加，负表示减
     * @param date
     * @param y 年
     * @param m 月
     * @param d 日
     * @param h 小时
     * @param mm 分钟
     */
    public static Date adjustTime(Date date,int y, int m, int d, int h, int mm, int ss) {
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

    /**
     * 把两个时间进行比较得出他们的间隔的天数，如果比较时间大于标准时间，则返回负的天数
     * @param compareTime 待比较的时间 String
     * @param standardTime 标准的时间 String
     * @return 返回比较的结果 int
     */
    public static int compareDifferDay(String compareTime,String standardTime){
        int result = 0;
        Date compareDate = strToDate(compareTime, "yyyy-MM-dd");
        Date standardDate = strToDate(standardTime, "yyyy-MM-dd");
        long sl = compareDate.getTime();
        long el = standardDate.getTime();
        long ei = el-sl;
        result = (int)((ei)/(1000*60*60*24));
        return result;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     *字符串的日期格式的计算
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 把两个时间进行比较得出他们的间隔的天数，如果比较时间大于标准时间，则返回负的天数
     * @param compareTime 待比较的时间 String
     * @param standardTime 标准的时间 String
     * @return 返回比较的结果 int
     */
    public static int compareDifferHour(String compareTime,String standardTime){
        int result = 0;
        Date compareDate = strToDate(compareTime, "HH:mm:ss");
        Date standardDate = strToDate(standardTime, "HH:mm:ss");
        long sl = compareDate.getTime();
        long el = standardDate.getTime();
        long ei = el-sl;
        result = (int)((ei)/(1000*60*60));
        return result;
    }

    /**
     * 把两个时间进行比较得出他们的间隔的小时数，如果比较时间大于标准时间，则返回负的小时数
     * @param compareTime
     * @param standardTime
     * @return
     * @author shenbh;
     * @date Sep 22, 2015 10:12:30 AM
     */
    public static int compareDifferHour(Date compareTime,Date standardTime){
        int result = 0;
        long sl = compareTime.getTime();
        long el = standardTime.getTime();
        long ei = el-sl;
        result = (int)((ei)/(1000*60*60));
        return result;
    }

    /**
     * 把两个时间进行比较得出他们的间隔的分钟数，如果比较时间大于标准时间，则返回负的分钟数
     *
     * @param compareTime
     * @param standardTime
     * @return
     */
    public static int compareDifferMins(Date compareTime, Date standardTime) {
        int result = 0;
        long sl = compareTime.getTime();
        long el = standardTime.getTime();
        long ei = el - sl;
        result = (int) ((ei) / (1000 * 60));
        return result;
    }

    /**
     * 把两个时间进行比较得出他们的间隔的秒数，如果比较时间大于标准时间，则返回负的秒数
     *
     * @param compareTime
     * @param standardTime
     * @return
     */
    public static long compareDifferSeconds(Date compareTime, Date standardTime) {
        long result = 0;
        long sl = compareTime.getTime();
        long el = standardTime.getTime();
        long ei = el - sl;
        result = ((ei) / (1000));
        return result;
    }


    /**
     * 返回指定日期中一周的所有日期
     * @param  date 具体日期yyyy-MM-dd
     * @return List<String>
     */
    public static List<String> getWeekDate(String date) {
        List<String> list = new ArrayList<String>();
        Calendar cal = new GregorianCalendar();
        cal.setTime(strToDate(date, "yyyy-MM-dd"));
        cal.add(Calendar.DAY_OF_WEEK, (1 - cal.get(Calendar.DAY_OF_WEEK)));
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 7; i++) {
            list.add(d.format(cal.getTime()));
            cal.roll(Calendar.DAY_OF_YEAR, true);
        }
        return list;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒
     * @param format
     * @return
     */
    public static String timeStamp2DateStr(Long seconds,String format) {
        seconds = seconds * 1000;
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒
     * @param format
     * @return
     */
    public static String timeStamp2DateStr(Integer seconds,String format) {
        seconds = seconds * 1000;
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(seconds));
    }

    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return long 精确到秒
     */
    public static Long dateStr2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime()/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static Long timeStamp(){
        long time = System.currentTimeMillis();
        return time/1000;
    }

    /**
     * 2023年05月24日 19:47:52
     */
    public static String dateStrByTime(Date date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdFormat.format(date);
        return str;
    }

    /**
     * 2023年05月24日 19:47:52
     */
    public static String dateStrByTimeShort(Date date) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String str = sdFormat.format(date);
        return str;
    }
}
