package com.reborn.hutubill.util;

import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    private static final long millisecondsOfOneDay = 1000*60*60*24;
    
    public static java.sql.Date util2Sql(java.util.Date d) {
        //java.util.Date转化成java.sql.Date
        return new java.sql.Date(d.getTime());
    }
    
    //获取今天的日期
    public static Date today() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }
    
    //获取月初
    public static Date monthBegin() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c.getTime();
        
    }
    
    //获取月末
    public static Date monthEnd() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
    
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONDAY, 1);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }
    
    //获取这个月总天数
    public static int thisMonthTotalDay() {
        long lastDayMillisSeconds = monthEnd().getTime();
        long firstDayMillisSeconds = monthBegin().getTime();
        
        return (int)((lastDayMillisSeconds-firstDayMillisSeconds)
                /millisecondsOfOneDay) + 1;
    }
    
    //获取本月剩余天数
    public static int thisMonthLeftDay() {
        long lastDayMillisSeconds = monthEnd().getTime();
        long todayMillisSeconds = today().getTime();
    
        return (int)((lastDayMillisSeconds-todayMillisSeconds)/millisecondsOfOneDay) + 1;
    }
    
    public static void main(String[] args) {
        System.out.println(DateUtil.monthBegin());
        System.out.println(DateUtil.monthEnd());
        System.out.println(DateUtil.today());
        System.out.println(DateUtil.thisMonthLeftDay());
        System.out.println(DateUtil.util2Sql(DateUtil.monthBegin()));
        System.out.println(DateUtil.util2Sql(DateUtil.monthEnd()));
        System.out.println(DateUtil.util2Sql(DateUtil.today()));
    }
}
