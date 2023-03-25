package com.example.android.moodplus.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import com.example.android.moodplus.R;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.loadUrl("https://poki.com/en/online");
    }
}