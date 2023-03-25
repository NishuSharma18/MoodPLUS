package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.R;

public class EmergencyActivity extends AppCompatActivity {

    private TextView emergencyCall1;
    private TextView visitWebsite1;
    private TextView emergencyCall2;
    private TextView visitWebsite2;
    private TextView emergencyCall3;
    private TextView visitWebsite3;
    private TextView emergencyCall4;
    private TextView visitWebsite4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        emergencyCall1 = findViewById(R.id.emergency_call_view1);
        visitWebsite1 = findViewById(R.id.emergency_site_view1);
        emergencyCall2 = findViewById(R.id.emergency_call_view2);
        visitWebsite2 = findViewById(R.id.emergency_site_view2);
        emergencyCall3 = findViewById(R.id.emergency_call_view3);
        visitWebsite3 = findViewById(R.id.emergency_site_view3);
        emergencyCall4 = findViewById(R.id.emergency_call_view4);
        visitWebsite4 = findViewById(R.id.emergency_site_view4);

        emergencyCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9152987821"));
                startActivity(intent);
            }
        });

        visitWebsite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.spif.in/i-am-feeling-suicidal/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        emergencyCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "116123"));
                startActivity(intent);
            }
        });

        visitWebsite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.samaritans.org/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        emergencyCall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "08000684141"));
                startActivity(intent);
            }
        });

        visitWebsite3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.papyrus-uk.org/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        emergencyCall4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "111"));
                startActivity(intent);
            }
        });

        visitWebsite4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://988lifeline.org/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }
}