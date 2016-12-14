package com.manipal.websis;


import android.util.Log;

public class DateUtils {

    public static String getProperDateMessage(String dateTime) {

        String date = getDay(dateTime);
        String time = getTime(dateTime);
        return date + getSuffix(date) + " " + getMonth(dateTime) + " at " + time;
    }

    private static String getDay(String dateTime) {
        return dateTime.substring(3, 5);
    }

    private static String getMonth(String dateTime) {
        String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Log.d("Month index", dateTime.substring(0, 2));
        return months[Integer.valueOf(dateTime.substring(0, 2)) - 1];
    }

    private static String getSuffix(String date) {
        int dt = (int) date.charAt(date.length() - 1);
        if (dt == 1)
            return "st";
        else if (dt == 2)
            return "nd";
        else if (dt == 3)
            return "rd";
        else
            return "th";
    }

    private static String getTime(String dateTime) {
        // 12/13/16 22:18
        return dateTime.substring(9, 14);
    }
}
