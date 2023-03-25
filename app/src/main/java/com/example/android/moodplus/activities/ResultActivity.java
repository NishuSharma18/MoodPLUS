package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.fragments.ChartsFragment;
import com.example.android.moodplus.fragments.MoodFragment;

public class ResultActivity extends AppCompatActivity {
    Button homeBtn ;
    TextView moodRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        moodRes = findViewById(R.id.moodResult);
        Float md =getIntent().getFloatExtra("getMood",0.0f);
        md = md/5;
        String mood ;
        if(md<0.01f){
            mood= "You said you've had a lot of negative thoughts. These thoughts are common when people feel down, angry or really stressed. Please speak to an adult right away.";
        }
        else if(md>=0.01f && md< 0.05f){
            mood ="It sounds like you are quite stressed and down.";
        }
        else if(md>=0.05f && md<0.1){
            mood= "It sounds like you are doing well, but you might be a bit stressed or down from time-to-time.";
        }
        else{
            mood= "It sounds like you are mostly doing well.";
        }
        moodRes.setText(mood);

        homeBtn = findViewById(R.id.buttonToHome);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this,GratitudeActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ChartsFragment.class);
        startActivity(intent);
        super.onBackPressed();
    }
}