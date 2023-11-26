package com.example.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeAgoUtil {

    public static String getTimeAgo(String timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date commentDate = dateFormat.parse(timestamp);

            long currentTime = System.currentTimeMillis();
            long commentTime = commentDate.getTime();
            long timeDifference = currentTime - commentTime;

            long seconds = timeDifference / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long weeks = days / 7;

            if (seconds < 60) {
                return "방금 전";
            } else if (minutes < 60) {
                return minutes + "분 전";
            } else if (hours < 24) {
                return hours + "시간 전";
            } else if (days < 7) {
                return days + "일 전";
            } else {
                return weeks + "주 전";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return timestamp;
        }
    }
}
