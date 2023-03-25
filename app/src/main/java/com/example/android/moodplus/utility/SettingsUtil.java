package com.example.android.moodplus.utility;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.android.moodplus.service.CaptureService;

public class SettingsUtil {

    public static void stopCaptureService(Context context) {
        Intent serviceIntent = new Intent(context, CaptureService.class);
        context.stopService(serviceIntent);
    }

    public static void startCaptureService(Context context) {
        Intent serviceIntent = new Intent(context, CaptureService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }

    public static boolean isCaptureServiceRunning(Context context) {
        String name = ContextCompat.getSystemServiceName(context, CaptureService.class);
        if (name == null || name.isEmpty())
            return false;
        Toast.makeText(context, name, Toast.LENGTH_LONG).show();
        return name.equals("ForegroundServiceChannel");
    }

}

