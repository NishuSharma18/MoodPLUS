package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.android.moodplus.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moodplus.R;
import com.example.android.moodplus.fragments.ChartsFragment;

public class GratitudeActivity extends AppCompatActivity {
    TextView clearTextBtn;
    EditText t1,t2,t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratitude);
        clearTextBtn = findViewById(R.id.clearTextButton);
        t1 = findViewById(R.id.editText1);
        t2= findViewById(R.id.editText2);
        t3 = findViewById(R.id.editText3);

        clearTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1.setText("");
                t2.setText("");
                t3.setText("");
            }
        });

    }
}