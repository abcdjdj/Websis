package com.manipal.websis;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RandomUtils {

    public static String getProperDateMessage(Date date) throws Exception {
        Log.d("RandomUtils.getProper()", date.toString());
        //Dec 19, 2016 20:08:10
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //String date = getDay(dateTime);
        //String time = getTime(dateTime);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        String dateFormat = SimpleDateFormat.getTimeInstance().format(date);
        return day + getSuffix(day) + " " + getMonth(month) + " at " + dateFormat;
    }


    private static String getMonth(int month) {
        String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    private static String getSuffix(int date) {
        int dt = date % 10;
        if (dt == 1)
            return "st";
        else if (dt == 2)
            return "nd";
        else if (dt == 3)
            return "rd";
        else
            return "th";
    }

    public static String toTitleCase(String str) {
        if (str == null) {
            return null;
        }
        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

}
