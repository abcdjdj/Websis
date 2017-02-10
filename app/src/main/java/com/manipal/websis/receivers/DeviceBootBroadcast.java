package com.manipal.websis.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;


public class DeviceBootBroadcast extends BroadcastReceiver {

    public static void registerAlarm(Context context) {
        Intent i = new Intent(context, RefreshDataBroadcast.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 69, i, 0);

        // We want the alarm to go off 3 seconds from now.
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1000 * 60 * 30;//start 3 seconds after first register.
        Log.d("Registering Alarm", "registerAlarm()");
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context
                .getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1000 * 60 * 60 * 4, sender);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            registerAlarm(context);

        }
    }
}
