package com.example.android.moodplus.model;

public class YouTubeVideo {
    private String VideoNo;
    private String VideoId;

    public YouTubeVideo(String videoNo, String videoId) {
        VideoNo = videoNo;
        VideoId = videoId;
    }

    public String getVideoNo() {
        return VideoNo;
    }

    public void setVideoNo(String videoNo) {
        VideoNo = videoNo;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }
}
