package com.example.android.moodplus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.android.moodplus.R;

public class PlayMusicMeditation extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    boolean isPlaying = false;
    private ImageView playSong;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music_meditation);

        playSong = findViewById(R.id.play_music_meditation1);
        Intent intent = getIntent();
        String songNumber = intent.getStringExtra("key");



        playSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    releaseMediaPlayer();
                    if(songNumber.equals("1")){
                        mMediaPlayer = MediaPlayer.create(getApplication(), R.raw.m1);
                    }
                    else if(songNumber.equals("2")){
                        mMediaPlayer = MediaPlayer.create(getApplication(), R.raw.m2);
                    }
                    else if(songNumber.equals("3")){
                        mMediaPlayer = MediaPlayer.create(getApplication(), R.raw.m3);
                    }
                    else if(songNumber.equals("4")){
                        mMediaPlayer = MediaPlayer.create(getApplication(), R.raw.m4);
                    }
                    mMediaPlayer.start();
                    playSong.setImageResource(R.drawable.pause);
                    isPlaying = true;

                } else {
                    mMediaPlayer.pause();
                    isPlaying = false;
                    playSong.setImageResource(R.drawable.play_music);
                }
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    // method for releasing media after being played
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}