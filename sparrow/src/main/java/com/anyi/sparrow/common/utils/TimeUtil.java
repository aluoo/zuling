package com.anyi.sparrow.common.utils;

import lombok.Data;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeUtil {


    private TimeUtil() {

    }


    public static Date timeAdd(Date date, Integer time, TemporalUnit unit) {

        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime other = localDateTime.plus(time, unit);
        Instant instant = other.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static long thisDaySecond(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1).with(LocalTime.MIN);
        return Duration.between(now, end).getSeconds();
    }

    public static void main(String[] args) {
        System.out.println(thisDaySecond());
    }
    /**
     * 查询当天还有多少分钟
     * @return
     */
    public static long thisDaysMinute(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1).with(LocalTime.MIN);
        return Duration.between(start, end).toMinutes();
    }

    public static String getDayOfFormat(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }


    @Data
    public static class Week {

        /**
         * 日期(2018-06-01~2018-06-10)
         **/
        private String day;

        /**
         * 开始日期
         **/
        private LocalDate startTime;
        /**
         * 结束日期
         **/
        private LocalDate endTime;
        /**
         * 开始日期数字化
         **/
        private Integer startTimeNumber;
        /**
         * 结束日期数字化
         **/
        private Integer endTimeNumber;

    }


    public static LocalDateTime getLastDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDateTime getFirstDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime getFirstDayOfWeek(LocalDateTime localDateTime) {
        TemporalAdjuster FIRST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(
                localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        return localDateTime.with(FIRST_OF_WEEK);
    }


    public static LocalDateTime getLastDayOfWeek(LocalDateTime localDateTime) {
        TemporalAdjuster LAST_OF_WEEK1 = TemporalAdjusters.ofDateAdjuster(
                localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));
        return localDateTime.with(LAST_OF_WEEK1);
    }



    /**
     * 根据开始时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Week> getWeekList(LocalDate startTime, LocalDate endTime) {

        List<Week> resultList = new ArrayList<Week>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        //判断是否同一周
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        if (startTime.get(weekFields.weekOfWeekBasedYear()) == endTime.get(weekFields.weekOfWeekBasedYear())) {
            Week firstWeek = new Week();
            firstWeek.setDay(startTime + "-" + endTime);
            firstWeek.setStartTime(startTime);
            firstWeek.setEndTime(endTime);
            firstWeek.setStartTimeNumber(Integer.valueOf(df.format(startTime)));
            firstWeek.setEndTimeNumber(Integer.valueOf(df.format(endTime)));
            resultList.add(firstWeek);
            return resultList;
        }
        //开始周
        TemporalAdjuster FIRST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(
                localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        LocalDate startFirstWeek = startTime.with(FIRST_OF_WEEK);  //开始周开始日期
        TemporalAdjuster LAST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(
                localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));
        LocalDate endFirstWeek = startTime.with(LAST_OF_WEEK);     //开始周结束日期

        //结束周
        TemporalAdjuster FIRST_OF_WEEK1 = TemporalAdjusters.ofDateAdjuster(
                localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        LocalDate startLastWeek = endTime.with(FIRST_OF_WEEK1);
        TemporalAdjuster LAST_OF_WEEK1 = TemporalAdjusters.ofDateAdjuster(
                localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));
        //将第一周添加
        Week firstWeek = new Week();
        firstWeek.setDay(startTime + "-" + endFirstWeek);
        firstWeek.setStartTime(startTime);
        firstWeek.setEndTime(endFirstWeek);
        firstWeek.setStartTimeNumber(Integer.valueOf(df.format(startTime)));
        firstWeek.setEndTimeNumber(Integer.valueOf(df.format(endFirstWeek)));
        resultList.add(firstWeek);
        while (true) {
            startFirstWeek = startFirstWeek.plusDays(7);
            if (startFirstWeek.with(LAST_OF_WEEK).equals(startLastWeek.with(LAST_OF_WEEK1))) {
                break;
            } else {
                Week week = new Week();
                week.setDay(startFirstWeek.with(FIRST_OF_WEEK) + "~" + startFirstWeek.with(LAST_OF_WEEK));
                week.setStartTime(startFirstWeek.with(FIRST_OF_WEEK));
                week.setEndTime(startFirstWeek.with(LAST_OF_WEEK));
                week.setStartTimeNumber(Integer.valueOf(df.format(startFirstWeek.with(FIRST_OF_WEEK))));
                week.setEndTimeNumber(Integer.valueOf(df.format(startFirstWeek.with(LAST_OF_WEEK))));
                resultList.add(week);
                //System.out.println("日期="+startFirstWeek+"开始周="+startFirstWeek.with(FIRST_OF_WEEK)+"结束周="+startFirstWeek.with(LAST_OF_WEEK));
            }
        }
        Week lastWeek = new Week();
        lastWeek.setDay(startLastWeek + "-" + endTime);
        lastWeek.setStartTime(startLastWeek);
        lastWeek.setEndTime(endTime);
        lastWeek.setStartTimeNumber(Integer.valueOf(df.format(startLastWeek)));
        lastWeek.setEndTimeNumber(Integer.valueOf(df.format(endTime)));
        resultList.add(lastWeek);
        return resultList;
    }


}
