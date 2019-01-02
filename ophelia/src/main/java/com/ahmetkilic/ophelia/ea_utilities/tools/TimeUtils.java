package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.content.Context;

import com.ahmetkilic.ophelia.R;
import com.ahmetkilic.ophelia.ea_utilities.enums.DateStyle;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class TimeUtils {

    public static Locale CURRENT_LOCALE;

    /**
     * If you want to use a specific locale in all TimeUtils functions, set it here.
     *
     * @param locale Locale to use as default locale
     */
    public static void setLocale(Locale locale) {
        CURRENT_LOCALE = locale;
    }

    /**
     * Remove Get default locale
     */
    public static Locale getLocale() {
        if (CURRENT_LOCALE == null)
            CURRENT_LOCALE = Locale.getDefault();
        return CURRENT_LOCALE;
    }

    /**
     * Add amount of hour to a date object
     *
     * @param date date to add hour
     * @param hour amount of hours
     */
    public static Date addHourToDate(Date date, int hour) {
        return new Date(date.getTime() + 3600000 * hour);
    }


    /**
     * Get year value from a date object
     *
     * @param date date object
     */
    public static int getYearFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Remove time values from a date object
     *
     * @param date date
     */
    public static Date getDateWithoutTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", getLocale());
        try {
            return format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * Get formatted date string with time
     *
     * @param date      date object
     * @param dateStyle date style
     * @param timeStyle time style
     */
    public static String getFormattedDateString(Date date, @DateStyle int dateStyle, @DateStyle int timeStyle) {
        if (date != null) {
            return DateFormat.getDateTimeInstance(dateStyle, timeStyle, getLocale()).format(date);
        } else
            return "";
    }

    /**
     * Get formatted date string
     *
     * @param date      date object
     * @param dateStyle date style
     */
    public static String getFormattedDateString(Date date, @DateStyle int dateStyle) {
        if (date != null) {
            return DateFormat.getDateInstance(dateStyle, getLocale()).format(date);
        } else
            return "";
    }

    /**
     * Get formatter date string for .Net Web service date objects.
     *
     * @param src date to format
     */
    public static String getDateStringForWeb(Date src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", getLocale());
        format.setTimeZone(TimeZone.getDefault());
        return format.format(src);
    }

    /**
     * Check if two date values are equals
     *
     * @param date1 date object
     * @param date2 date object
     */
    public static boolean isDatesEqual(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;

        long julianDayNumber1 = date1.getTime();
        long julianDayNumber2 = date2.getTime();

        return julianDayNumber1 == julianDayNumber2;
    }

    /**
     * Check if two date values are equals without time of day
     *
     * @param date1 date object
     * @param date2 date object
     */
    public static boolean isDatesEqualWithoutTime(Date date1, Date date2) {
        if (date1 == null || date2 == null)
            return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", getLocale());

        try {
            date1 = format.parse(format.format(date1));
            date2 = format.parse(format.format(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long julianDayNumber1 = date1.getTime();
        long julianDayNumber2 = date2.getTime();

        return julianDayNumber1 == julianDayNumber2;
    }

    /**
     * Generate a formatted string from a value of millis duration as Day, Hour, Minutes, millis
     *
     * @param duration duration
     * @param context  context to get string from resources
     */
    public static String millisToShortDHMS(long duration, Context context) {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));

        if (days > 0)
            res += String.valueOf(days) + " " + context.getString(R.string.day) + ", ";
        if (hours > 0)
            res += String.valueOf(hours) + " " + context.getString(R.string.hour) + ", ";
        if (minutes > 0)
            res += String.valueOf(minutes) + " " + context.getString(R.string.minute) + ", ";
        if (seconds > 0)
            res += String.valueOf(seconds) + " " + context.getString(R.string.second);

        return res;
    }

    /**
     * Generate a formatted string from a value of seconds duration as Day, Hour, Minutes, millis
     *
     * @param duration duration
     * @param context  context to get string from resources
     */
    public static String secondsToShortDHMS(long duration, Context context) {
        String res = "";
        duration = duration * 1000;
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));

        if (days > 0)
            res += String.valueOf(days) + " " + context.getString(R.string.day) + ", ";
        if (hours > 0)
            res += String.valueOf(hours) + " " + context.getString(R.string.hour) + ", ";
        if (minutes > 0)
            res += String.valueOf(minutes) + " " + context.getString(R.string.minute) + ", ";
        if (seconds > 0)
            res += String.valueOf(seconds) + " " + context.getString(R.string.second);

        return res;
    }
}
