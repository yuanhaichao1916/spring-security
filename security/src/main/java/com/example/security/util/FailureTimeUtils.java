package com.example.security.util;

import com.example.security.enums.FailureTime;

import java.util.Calendar;
import java.util.Date;

public class FailureTimeUtils {
    public static Date createValidTime(FailureTime failureTime, int jwtValidTime) {
        Date date = new Date();
        if (failureTime.name().equals(FailureTime.SECOND)) {
            return createBySecond(date, jwtValidTime);
        }
        if (failureTime.name().equals(FailureTime.MINUTE)) {
            return createBySecond(date, jwtValidTime * 60);
        }
        if (failureTime.name().equals(FailureTime.HOUR)) {
            return createBySecond(date, jwtValidTime * 60 * 60);
        }
        if (failureTime.name().equals(FailureTime.DAY)) {
            return getDateAfter(date, jwtValidTime);
        }
        return null;
    }

    /**
     * This method is used to get the date a few days later。
     *
     * @param date
     * @param day
     * @return
     */
    private static Date getDateAfter(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.DATE, instance.get(Calendar.DATE) + day);
        return instance.getTime();
    }

    /**
     * This method is used to obtain data from the previous few days.
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDateBefore(Date date, int day) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.DATE, instance.get(Calendar.DATE) - day);
        return instance.getTime();
    }

    /**
     * This method use to get valid data by second.
     *
     * @param date         data
     * @param jwtValidTime jwt
     * @return data
     */
    private static Date createBySecond(Date date, int jwtValidTime) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.SECOND, jwtValidTime);
        return instance.getTime();
    }
}
