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

    private static String checkRoman(String str) {
        switch (str.toUpperCase()) {
            case "I":
                return "1";
            case "II":
                return "2";
            case "III":
                return "3";
            case "IV":
                return "4";
            case "V":
                return "5";
            default:
                return str;
        }

    }

    public static String toTitleCase(String str) {
        String[] strs = str.split(" ");
        String newstr = "";
        for (int i = 0; i < strs.length; i++) {
            strs[i] = checkRoman(strs[i]);
            char tmp[] = strs[i].toCharArray();
            for (int j = 1; j < tmp.length; j++) {
                tmp[j] = Character.toLowerCase(tmp[j]);
            }
            newstr += new String(tmp) + " ";
        }
        return newstr;
    }

}
