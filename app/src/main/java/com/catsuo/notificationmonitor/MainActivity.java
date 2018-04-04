package com.catsuo.notificationmonitor;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int NOTIFICATION_ID = 1;
    private NotificationManager mNM = null;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        findViewById(R.id.notify_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNM.notify(NOTIFICATION_ID, new Notification.Builder(MainActivity.this).setTicker("ticker test").setSmallIcon(R.mipmap.ic_launcher_round).setContentText("content test").build());
            }
        });

        if (!isNotificationListenerEnabled(this)) {
            openNotificationListenSettings();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                NotificationListener.requestRebind(new ComponentName("com.catsuo.notificationmonitor", "com.catsuo.notificationmonitor.NotificationListener"));
            } else {
                PackageManager pm = getPackageManager();
                pm.setComponentEnabledSetting(new ComponentName("com.catsuo.notificationmonitor", "com.catsuo.notificationmonitor.NotificationListener"),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                pm.setComponentEnabledSetting(new ComponentName("com.catsuo.notificationmonitor", "com.catsuo.notificationmonitor.NotificationListener"),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            }
            Log.i(TAG, "NotificationListenerService already is enable");
        }
    }

    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    public void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
