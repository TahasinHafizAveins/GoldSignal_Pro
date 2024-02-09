package com.example.goldsignalpro.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class utils {
    public static int compareVersionNames(String installed_app_version, String api_app_version){
        int res = 0;

        String[] oldNumbers = installed_app_version.split("\\.");
        String[] newNumbers = api_app_version.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
    }

    public static String getFormattedTime(String created_at) {
        String formattedDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm aa, dd MMMM,yyyy", Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+12:00"));
        Date date = null;
        try {
            date = inputFormat.parse(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = outputFormat.format(date);
        }
        return formattedDate;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+6:00"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public static String getRemainingTime_formate(String remaining_time) {
        String formattedDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+6:00"));
        Date date = null;
        try {
            date = inputFormat.parse(remaining_time);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date != null) {
            formattedDate = outputFormat.format(date);

        }
        return formattedDate;

    }

    public static String calculateRemainingTime(String api_time) {
        String duration = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            Date d1 = sdf.parse(getCurrentDateTime());
            Date d2 = sdf.parse(getRemainingTime_formate(api_time));

            // Calculate time difference
            // in milliseconds
            long difference_In_Time
                    = d1.getTime() - d2.getTime();

            // Calculate time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            long difference_In_Years
                    = (difference_In_Time
                    / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days
                    = (difference_In_Time
                    / (1000 * 60 * 60 * 24))
                    % 365;


            if (Math.abs(difference_In_Days) > 0) {
                if (Math.abs(difference_In_Hours) > 0) {
                    duration = String.format( "%d", Math.abs(difference_In_Days)) + " Days, " + String.format( "%d",  Math.abs(difference_In_Hours)) + " hours";
                } else {
                    duration = String.format( "%d", Math.abs(difference_In_Days)) + " Days";
                }
            } else {
                if (Math.abs(difference_In_Hours) > 0) {
                    duration = String.format( "%d",  Math.abs(difference_In_Hours)) + " hours, " + String.format( "%d",  Math.abs(difference_In_Minutes)) + " minutes";
                } else {
                    duration = String.format( "%d",  Math.abs(difference_In_Minutes)) + " minutes";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            duration = "0";
        }

        return duration;
    }

}
