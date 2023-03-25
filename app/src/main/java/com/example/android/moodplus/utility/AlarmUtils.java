package com.example.android.moodplus.utility;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import com.example.android.moodplus.service.NotificationReceiver;

public class AlarmUtils {

    public static void setAlarm(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,F);

        // Set the alarm to trigger every 4 hours
        int interval = 4 * 60 * 60 * 1000; // 4 hours in milliseconds
        long firstTriggerTime = System.currentTimeMillis() + interval;
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstTriggerTime, interval, pendingIntent);
    }
}
