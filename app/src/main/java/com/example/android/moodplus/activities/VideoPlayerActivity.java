package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.view.View;
import android.widget.ProgressBar;
import android.os.Bundle;

import com.example.android.moodplus.R;
import com.example.android.moodplus.model.YouTubeVideo;
import com.example.android.moodplus.adapter.YoutubeVideoAdapter;


public class VideoPlayerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<YouTubeVideo> youTubeVideos;
    private ProgressBar progressBar;
    private YoutubeVideoAdapter youTubeVideoAdapter;
    YoutubeVideoAdapter.OnVideosClickListener onVideosClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        progressBar = findViewById(R.id.progressbar);
        youTubeVideos = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        getYoutubeVideos();
    }

    private void getYoutubeVideos(){
        YouTubeVideo youTubeVideo = new YouTubeVideo("0","KxQhx_tcCf8");
        YouTubeVideo youTubeVideo1 = new YouTubeVideo("1","3AtDnEC4zak");
        youTubeVideos.add(youTubeVideo);
        youTubeVideos.add(youTubeVideo1);
        youTubeVideoAdapter = new YoutubeVideoAdapter(youTubeVideos,VideoPlayerActivity.this,onVideosClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoPlayerActivity.this));
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(youTubeVideoAdapter);
    }

}