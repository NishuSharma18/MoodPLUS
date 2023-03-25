package com.example.android.moodplus.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moodplus.model.YouTubeVideo;
import com.example.android.moodplus.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.VideosHolder> {

    private ArrayList<YouTubeVideo> youTubeVideos;
    private Context context;
    private OnVideosClickListener onVideosClickListener;

    public YoutubeVideoAdapter(ArrayList<YouTubeVideo> youTubeVideos, Context context, OnVideosClickListener onVideosClickListener) {
        this.youTubeVideos = youTubeVideos;
        this.context = context;
        this.onVideosClickListener = onVideosClickListener;
    }

    public interface OnVideosClickListener{
        void onVideosClicked(int position);
    }

    @NonNull
    @Override
    public VideosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_holder,parent,false);
        return new VideosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = youTubeVideos.get(position).getVideoId();
//                Toast.makeText(context, videoId, Toast.LENGTH_SHORT).show();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return youTubeVideos.size();
    }

    class VideosHolder extends RecyclerView.ViewHolder{

        YouTubePlayerView youTubePlayerView;

        public VideosHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onVideosClickListener.onVideosClicked(getAdapterPosition());
                }
            });

            youTubePlayerView = itemView.findViewById(R.id.youtubePlayerId);
        }
    }

}
