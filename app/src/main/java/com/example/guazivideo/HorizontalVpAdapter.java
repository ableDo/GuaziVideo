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

import com.example.guazivideo.player.GuaziPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * CreateTime: 2020/1/15 15:06
 * Author: hxd
 * Content:
 * UpdateTime:
 * UpdateName;
 * UpdateContent:
 */
public class HorizontalVpAdapter extends RecyclerView.Adapter<HorizontalVpAdapter.HorizontalVpViewHolder> {

    private List<String> sources;
    private Context mContext;
    private GuaziPlayer tempPlayer = null;
    private int tempPosition = 0;


    HorizontalVpAdapter(Context context) {
        mContext = context;
        if (sources == null) {
            sources = new ArrayList<>();
            sources.add("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
            sources.add("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
            sources.add("http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4");
            sources.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4");
            sources.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4");
        }
    }

    @Override
    public HorizontalVpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("adapter", "create view holoder");
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

        holder.videoPlayer.changeSourceAndPlay(getSource(position));
        tempPlayer = holder.videoPlayer;

    }
    public String getSource(int position) {
        return sources.get(position);
    }
    public void setTempPosition(int position) {
        tempPosition = position;
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
