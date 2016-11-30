package com.calf.frame.utils;


import com.calf.frame.storage.CacheManager;

import java.util.Calendar;
import java.util.Date;

/**
 * @author JinYi Liu
 */
public class DateUtils {

    public static boolean isDelayMills(long cacheMillis, long expiredMillis) {
        return isDelayMills(System.currentTimeMillis(), cacheMillis, expiredMillis);
    }

    public static boolean isDelayMills(Date cacheDate, long expiredMillis) {
        return isDelayMills(System.currentTimeMillis(), cacheDate.getTime(), expiredMillis);
    }

    protected static boolean isDelayMills(long currentMillis, long cacheMillis, long expiredMillis) {
        boolean flag = false;
        if (currentMillis - cacheMillis >= expiredMillis) {
            flag = true;
        }
        return flag;
    }

    public static boolean isApartMills(long cacheMillis, long expiredMillis) {
        return isApartMills(new Date(cacheMillis), expiredMillis);
    }

    public static boolean isApartMills(Date cacheDate, long expiredMillis) {
        return isApartMills(System.currentTimeMillis(), cacheDate, expiredMillis);
    }

    protected static boolean isApartMills(long currentMillis, Date cacheDate, long expiredMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cacheDate);
        long cacheMillis = resetByExpiredMillis(calendar, expiredMillis);
        return currentMillis - cacheMillis >= expiredMillis;
    }

    private static long resetByExpiredMillis(Calendar calendar, long expiredMillis) {
        if (expiredMillis >= CacheManager.DAY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else if (expiredMillis >= CacheManager.HOUR) {
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else if (expiredMillis >= CacheManager.MINUTE) {
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar.getTimeInMillis();
    }

}
