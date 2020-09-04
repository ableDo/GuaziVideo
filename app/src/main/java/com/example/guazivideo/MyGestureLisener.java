package com.example.guazivideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.guazivideo.activity.MainActivity;
import com.example.guazivideo.activity.MoreInformationActivity;
import com.example.guazivideo.adapter.HorizontalVpAdapter;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.player.GuaziPlayer;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class MyGestureLisener extends GestureDetector.SimpleOnGestureListener {

    private Context context;
    private ViewPager2 viewPager2;
    private HorizontalVpAdapter adapter;
    private boolean isFullVideo = false;
    private boolean isGesturesolving = false;

    public MyGestureLisener(Context context, ViewPager2 viewPager2, HorizontalVpAdapter adapter ) {
        this.context = context;
        this.viewPager2 = viewPager2;
        this.adapter = adapter;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.i("Gesture", "favor");
        Toast.makeText(context, "喜欢", Toast.LENGTH_LONG).show();
        gestureFavor();
        return super.onDoubleTap(e);
    }

    // 4. 用户轻击屏幕后抬起
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("MyGesture", "onSingleTapUp");
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);
        videoPlayer.clickOnce();
        return true;
    }


    // 用户按下触摸屏、快速移动后松开
    // 参数：
    // e1：第1个ACTION_DOWN MotionEvent
    // e2：最后一个ACTION_MOVE MotionEvent
    // velocityX：X轴上的移动速度，像素/秒
    // velocityY：Y轴上的移动速度，像素/秒
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        Log.i("MyGesture", "onFling" + velocityX + " " + velocityY);
        //右滑
        if (velocityX > 0 && Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
            Log.i("Gesture", "right");
            Toast.makeText(context, "右滑", Toast.LENGTH_LONG).show();
            gestureRight();
        }
        //下滑
        if (velocityY > 0 && Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
            Log.i("Gesture", "down");
            Toast.makeText(context, "下滑", Toast.LENGTH_LONG).show();
            gestureDown();
        }
        //左滑
        if (velocityX < 0 && Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
            Log.i("Gesture", "left");
            Toast.makeText(context, "左滑", Toast.LENGTH_LONG).show();
            getstureLeft();
        }
        //上滑
        if (velocityY < 0 && Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
            Log.i("Gesture", "up");
            Toast.makeText(context, "上滑", Toast.LENGTH_LONG).show();
            gestureUp();
        }
        return true;
    }


    public void gestureFavor() {

    }
    public void gestureOK() {
        if (!isFullVideo) {
            getstureLeft();
        } else {
            gestureRight();
        }

    }
    public void gesturePalm() {
        gestureRight();
    }


    public void gestureUp() {
        if (adapter.getItemCount() > viewPager2.getCurrentItem() + 1 && !isFullVideo && !isGesturesolving) {
            isGesturesolving = true;
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(context,R.anim.animation_up);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    adapter.setTempPosition(viewPager2.getCurrentItem() + 1);
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, false);
                    adapter.notifyDataSetChanged();
                    isGesturesolving = false;
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            videoPlayer.startAnimation(animation);


        }

    }

    public void gestureDown() {

        if (viewPager2.getCurrentItem() > 0 && !isFullVideo && !isGesturesolving) {
            isGesturesolving = true;
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(context,R.anim.animation_down);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    adapter.setTempPosition(viewPager2.getCurrentItem() - 1);
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1, false);
                    adapter.notifyDataSetChanged();
                    isGesturesolving = false;
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }

            });
            videoPlayer.startAnimation(animation);
        }

    }



    public void gestureRight() {
        if (isGesturesolving) {
            return;
        }
        isGesturesolving = true;
        if (isFullVideo) {
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_right);
            videoPlayer.startAnimation(animation);

            VideoInfo videoInfo = adapter.getVideoInfoByPosition(viewPager2.getCurrentItem());
            videoPlayer.release();
            videoPlayer.changeSourceAndPlay(videoInfo.getShort_video_info().getVideo_1().getUrl(), videoInfo.getShort_title(), false, true);
            isFullVideo = false;
        } else {
            //进入详情页
            Intent intent = new Intent(context, MoreInformationActivity.class);
            intent.putExtra("video_info", adapter.getVideoInfoByPosition(viewPager2.getCurrentItem()));
            context.startActivity(intent);
            Activity  activity = (Activity) context;
            activity.overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
        }
        isGesturesolving  = false;
    }

    public void getstureLeft() {
        if (isGesturesolving) {
            return;
        }
        isGesturesolving = true;
        if (!isFullVideo)  {
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(context,R.anim.animation_left);
            videoPlayer.startAnimation(animation);

            VideoInfo videoInfo = adapter.getVideoInfoByPosition(viewPager2.getCurrentItem());
            videoPlayer.release();
            videoPlayer.changeSourceAndPlay(videoInfo.getMovie_video_info().getVideo_1().getUrl(), videoInfo.getMovie_name(), true, false);
            videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gestureRight();
                }
            });
            isFullVideo = true;
        }
        isGesturesolving = false;
    }


}
