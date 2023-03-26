package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.activities.CallActivity;

public class SuggestionCallActivity extends AppCompatActivity {

    TextView chatTextView;
    TextView callActivity_tv;
    TextView sadtv;
    int mood = 1;
    //    int mood = 0; //Happy
//    int mood = 1; //sad
//    int mood = 2; //fear,anxious
//    int mood = 3; //angry
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_call);
        chatTextView = findViewById(R.id.chat);
        sadtv = findViewById(R.id.sad_tv);
        callActivity_tv = findViewById(R.id.sadReply_tv1);

        chatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatTextView.setVisibility(View.GONE);
                if(mood==0){

                }
                else if(mood==1){
                    sadtv.setVisibility(View.VISIBLE);
                    callActivity_tv.setVisibility(View.VISIBLE);
                }
                else if(mood==2){

                }
                else{

                }
            }
        });

        callActivity_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                startActivity(intent);
            }
        });
    }
}