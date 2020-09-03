package com.example.guazivideo.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class GuaziPlayer extends StandardGSYVideoPlayer {

    public GuaziPlayer(Context context) {
        super(context);
    }

    public GuaziPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void clickOnce() {
        super.touchDoubleUp();
    }


    public void doubleClick() {

    }

    public void release() {
        this.releaseVideos();
    }

    public void changeSourceAndPlay(String url, String title, boolean isBackBtnVisible) {

        this.setUp(url, true, title);

        //增加title
        this.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        if (isBackBtnVisible) {
            this.getBackButton().setVisibility(View.VISIBLE);
        } else {
            this.getBackButton().setVisibility(View.INVISIBLE);
        }


        this.setIsTouchWiget(true);

        this.startPlayLogic();
    }

}
