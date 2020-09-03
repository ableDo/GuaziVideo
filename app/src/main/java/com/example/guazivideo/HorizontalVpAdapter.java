package com.example.guazivideo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guazivideo.entity.MovieVideoInfo;
import com.example.guazivideo.entity.Video;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.player.GuaziPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class HorizontalVpAdapter extends RecyclerView.Adapter<HorizontalVpAdapter.HorizontalVpViewHolder> {

    private List<VideoInfo> sources;
    private Context mContext;
    private GuaziPlayer tempPlayer = null;
    private int tempPosition = 0;
    private final String TAG = "adapter";

    public HorizontalVpAdapter(Context context, List<VideoInfo> sources) {
        mContext = context;
        this.sources = sources;
    }

    @Override
    public HorizontalVpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "create view holoder");
        return new HorizontalVpViewHolder(LayoutInflater.from(mContext).inflate((R.layout.activity_simple_play), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalVpViewHolder holder, int position) {
        System.out.println("onbind" + position);
        if (position  != tempPosition)  {
            return;
        }
        if (tempPlayer != null) {
            tempPlayer.release();
        }

        holder.videoPlayer.changeSourceAndPlay(getVideoUrl(position), getTitle(position), false);
        Log.i(TAG, getVideoUrl(position));
        tempPlayer = holder.videoPlayer;

    }
    private String getVideoUrl(int position) {
        return sources.get(position).getShort_video_info().getVideo_1().getUrl();
    }
    private String getTitle(int position) {
        return sources.get(position).getShort_title();
    }

    public VideoInfo getVideoInfoByPosition(int position) {
        return sources.get(position);
    }

    public void setTempPosition(int position) {
        tempPosition = position;
    }

    public MovieVideoInfo getFullVideo(int position) {
        return sources.get(position).getMovie_video_info();
    }

    @Override
    public int getItemCount() {
        if (sources == null) {
            return 0;
        }
        return sources.size();
    }

    class HorizontalVpViewHolder extends RecyclerView.ViewHolder {
        GuaziPlayer videoPlayer;


        HorizontalVpViewHolder(@NonNull View itemView) {
            super(itemView);
            videoPlayer = itemView.findViewById(R.id.video_player);
        }
    }


}
