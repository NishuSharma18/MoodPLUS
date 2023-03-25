package com.example.android.moodplus.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.android.moodplus.R;
import com.example.android.moodplus.fragments.MoodFragment;

public class CaptureService extends Service {

    final private String CHANNEL_ID = "ForegroundServiceChannel";
    private BroadcastReceiver br;

    public CaptureService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MoodFragment.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("EmotionDetector")
                        .setContentText("Captures images on device unlock using front camera")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.app_name))
                        .build();

        startForeground(1, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        br = new UnlockReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        filter.addAction();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//            filter.addAction(Intent.ACTION_USER_UNLOCKED);
        this.registerReceiver(br, filter);
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(br);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Foreground Service Channel";
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
