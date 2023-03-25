package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.fragments.MoodFragment;

public class SplashScreenActivity extends AppCompatActivity {
    TextView splashText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashText= findViewById(R.id.splashText);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);
                finish();
            }
        },3000);
    }
}