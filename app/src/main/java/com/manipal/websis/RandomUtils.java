package com.manipal.websis;


public class RandomUtils {

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
        return dateTime.substring(9, 14);
    }

    public static String toTitleCase(String str) {
        if (str == null) {
            return null;
        }
        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i=0; i < len; ++i) {
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
