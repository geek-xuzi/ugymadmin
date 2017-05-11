package com.ugym.admin.bean;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public class CacheKey {

    private int month;
    private int day;
    private int weekDay;

    public CacheKey(int month, int day, int weekDay) {
        this.month = month;
        this.day = day;
        this.weekDay = weekDay;
    }


    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
}
