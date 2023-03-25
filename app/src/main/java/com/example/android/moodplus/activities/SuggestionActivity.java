package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.android.moodplus.R;

import java.util.Objects;

public class SuggestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String mood = getIntent().getStringExtra("getMood");

        TextView videoHp = findViewById(R.id.video_sugg_activity);
        videoHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestionActivity.this, VideoPlayerActivity.class);
                startActivity(intent);
            }
        });

        TextView musicBreathHp = findViewById(R.id.breath_sugg_activity);
        musicBreathHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestionActivity.this, MusicBreathingActivity.class);
                startActivity(intent);
            }
        });

        TextView musicMeditationHp = findViewById(R.id.meditation_sugg_activity);
        musicMeditationHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestionActivity.this, MeditationActivity.class);
                startActivity(intent);
            }
        });

        TextView textView = findViewById(R.id.games_sugg_activity);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(intent);
            }
        });

        TextView musictextview = findViewById(R.id.music_sugg_activity);
        musictextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionActivity.this,MusicActivity.class);
                intent.putExtra("moodfromSuggestionActivity",mood);
                startActivity(intent);
            }
        });

        TextView maptextview = findViewById(R.id.maps_sugg_activity);
        maptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionActivity.this,NearByLocationActivity.class);
                startActivity(intent);
            }
        });

        TextView gratitude_tv = findViewById(R.id.gratitude_sugg_activtiy);
        gratitude_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionActivity.this,GratitudeActivity.class);
                startActivity(intent);
            }
        });

        TextView thoughts_tv = findViewById(R.id.journal_sugg_activtiy);
        thoughts_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionActivity.this,ThoughtNoteActivity.class);
                startActivity(intent);
            }
        });

        TextView call_tv = findViewById(R.id.connect_sugg_activity);
        call_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestionActivity.this,CallActivity.class);
                startActivity(intent);
            }
        });

    }
}