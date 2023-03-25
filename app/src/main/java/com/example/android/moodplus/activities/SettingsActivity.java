package com.example.android.moodplus.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.android.moodplus.R;
import com.example.android.moodplus.helper.DBHelper;
import com.example.android.moodplus.utility.SettingsUtil;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    DBHelper db;

    SwitchCompat toggleSwitch;
    private ArrayList<Float> smileList;
    private ArrayList<String> emotionsList;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleSwitch = findViewById(R.id.toggle_service_btn);

        db = new DBHelper(this);
        smileList = new ArrayList<>();
        emotionsList = new ArrayList<>();

        if (SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Boolean switchState = sharedPreferences.getBoolean("switchState",false);
        if(switchState){
            toggleSwitch.toggle();
        }

        toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(toggleSwitch.isChecked()){
                    SettingsUtil.startCaptureService(getApplicationContext());
                    editor.putBoolean("switchState",true);
                }
                else{
                    SettingsUtil.stopCaptureService(getApplicationContext());
                    editor.putBoolean("switchState",false);
                }
                editor.apply();
            }
        });
    }
}