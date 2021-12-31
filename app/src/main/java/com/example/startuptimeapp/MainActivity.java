package com.example.startuptimeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    ListView app_log;
    List<AppLog> appLogList;
    ListAdapter listAdapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_log = findViewById(R.id.app_log);


        if (!checkReadStatsPermission()) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }


        UsageStatsManager usageStatsManager =
                (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        List<UsageStats> usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                calendar.getTimeInMillis(),
                System.currentTimeMillis()
        );
        appLogList = usageStats.stream().filter(u -> u.getTotalTimeInForeground() != 0).map(u -> new AppLog(u.getPackageName(), u.getTotalTimeInForeground(), u.getFirstTimeStamp(), u.getLastTimeUsed())).collect(Collectors.toList());
        listAdapters = new ListAdapter();
        app_log.setAdapter(listAdapters);


    }

    private boolean checkReadStatsPermission() {
        // AppOpsManagerを取得
        AppOpsManager aom = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        // GET_USAGE_STATSのステータスを取得
        int mode = aom.checkOp(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            // AppOpsの状態がデフォルトなら通常のpermissionチェックを行う。
            // 普通のアプリならfalse
            return checkPermission("android.permission.PACKAGE_USAGE_STATS", android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED;
        }
        // AppOpsの状態がデフォルトでないならallowedのみtrue
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appLogList.size();
        }

        @Override
        public Object getItem(int i) {
            return appLogList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView iconImageView;
            TextView appNameTextView;
            TextView totalTimeTextView;
            TextView firstTimeTextView;
            TextView lastTimeTextView;

            View v = view;
            if(Objects.isNull(v)) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.app_list, null);
            }

            AppLog appLog = (AppLog) getItem(i);
            if(Objects.nonNull(appLog)) {
                iconImageView = (ImageView)v.findViewById(R.id.icon);
                appNameTextView = (TextView) v.findViewById(R.id.app_name);
                totalTimeTextView = (TextView) v.findViewById(R.id.total_time);
                firstTimeTextView = (TextView) v.findViewById(R.id.first_time);
                lastTimeTextView = (TextView) v.findViewById(R.id.last_Time);

                appNameTextView.setText(appLog.getPackageName());
                totalTimeTextView.setText(appLog.getTotalTimeInForeground());
                firstTimeTextView.setText(appLog.getFirstTimeStamp());
                lastTimeTextView.setText(appLog.getLastTimeUsed());
            }
            return v;
        }
    }
}