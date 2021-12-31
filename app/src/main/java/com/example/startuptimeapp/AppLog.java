package com.example.startuptimeapp;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppLog {

    private String packageName;

    private Long totalTimeInForeground;

    private Long firstTimeStamp;

    private Long lastTimeUsed;

    public AppLog(String packageName, Long totalTimeInForeground, Long firstTimeStamp, Long lastTimeUsed) {
        this.packageName = packageName;
        this.totalTimeInForeground = totalTimeInForeground;
        this.firstTimeStamp = firstTimeStamp;
        this.lastTimeUsed = lastTimeUsed;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getTotalTimeInForeground() { return getStringDate(totalTimeInForeground).substring(12); }

    public String getFirstTimeStamp() {
        return getStringDate(firstTimeStamp);
    }

    public String getLastTimeUsed() {
        return getStringDate(lastTimeUsed);
    }

    private String getStringDate(long milliseconds) {
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE);
        final Date date = new Date(milliseconds);
        return df.format(date);
    }
}