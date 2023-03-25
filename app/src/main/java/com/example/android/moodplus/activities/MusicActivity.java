package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.android.moodplus.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.android.moodplus.R;

import java.util.Objects;

public class MusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        Objects.requireNonNull(getSupportActionBar()).hide();
        WebView myWebView = (WebView) findViewById(R.id.webView);

        String mood = getIntent().getStringExtra("moodfromSuggestionActivity");
        if(mood=="Happy") {
            myWebView.loadUrl("https://open.spotify.com/playlist/37i9dQZF1DWZKuerrwoAGz");
        }
        else if(mood =="Sad"){
            myWebView.loadUrl("https://open.spotify.com/playlist/37i9dQZF1DWSqBruwoIXkA");
        }
        else if(mood =="Anger"){
            myWebView.loadUrl("https://open.spotify.com/playlist/37i9dQZF1DX4sWSpwq3LiO");
        }
        else if(mood =="Fear"){
            myWebView.loadUrl("https://open.spotify.com/playlist/37i9dQZF1DX8Uebhn9wzrS");
        }
        else{
            myWebView.loadUrl("https://open.spotify.com/playlist/37i9dQZF1DX4WYpdgoIcn6");
        }
    }
}