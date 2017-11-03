package com.mnnyang.gzuclassschedule.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by mnnyang on 17-11-2.
 */

public class TimeUtils {
    /**
     * 返回某一周的第一周距离现在的实际周数
     *
     * @param weekBeginMillis
     * @param endMillis
     * @return
     */
    public static int getWeekGap(long weekBeginMillis, long endMillis) {
        return (int) (((endMillis - weekBeginMillis) / (1000 * 3600 * 24)) / 7);
    }

    /**
     * 获取本周周一的日期
     *
     * @return
     */
    public static Date getNowWeekBegin() {
        int mondayPlus;
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 1) {
            mondayPlus = 0;
        } else {
            mondayPlus = 1 - dayOfWeek;
        }
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);

        return currentDate.getTime();
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int getNowMonth() {
        Calendar calendar = Calendar.getInstance();
        return 1 + calendar.get(Calendar.MONTH);
    }
}
