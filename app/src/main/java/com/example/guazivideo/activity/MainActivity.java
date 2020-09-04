package com.example.guazivideo.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import site.gemus.openingstartanimation.LineDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.guazivideo.MyGestureLisener;
import com.example.guazivideo.adapter.HorizontalVpAdapter;
import com.example.guazivideo.R;
import com.example.guazivideo.mvp.base.BaseActivity;
import com.example.guazivideo.entity.Changer;
import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;

import com.example.guazivideo.mvp.base.BaseMvpActivity;
import com.example.guazivideo.mvp.contract.MainContract;
import com.example.guazivideo.mvp.presenter.MainPresenter;
import com.example.guazivideo.player.GuaziPlayer;
import com.example.guazivideo.net.VideoService;
import com.example.guazivideo.net.WebService;
import com.example.guazivideo.androidview.RotationLoadingView;

import java.lang.ref.WeakReference;
import java.util.List;
import org.tensorflow.lite.examples.classification.CameracCassificationInterface.ResultHandler;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View{


    private ViewPager2 viewPager2;
    private HorizontalVpAdapter adapter;
    public static final int START_TIME = 2000;

    private MyGestureLisener myGestureLisener = null;

    private View backGroundView;
    private RotationLoadingView loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setTimer();
        initView();
        mPresenter.requestVideo();
        mPresenter.startRequestChangerAndGesture();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);
        videoPlayer.clickOnce();
        setSystemUIVisible(false);
        mPresenter.startDetector(R.id.main_container, R.layout.tfe_ic_camera_connection_fragment);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter.getItemCount() > 0) {
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);
            videoPlayer.clickOnce();
        }

        mPresenter.stopDetector();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }




    public void initView() {
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setDrawStategy(new LineDrawStrategy()) //设置动画效果
                .setAnimationFinishTime(START_TIME)
                .create();
        openingStartAnimation.show(this);
        backGroundView = findViewById(R.id.main_background);
        loadingView  = findViewById(R.id.item_loading_image);
        loadingView.startRotationAnimation();

    }
    private void initViewPager(List<VideoInfo> videoInfos) {
        viewPager2 = findViewById(R.id.vp_h);
        adapter = new HorizontalVpAdapter(this, videoInfos);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(adapter);

    }

    public void initDetector() {
        myGestureLisener = new MyGestureLisener(MainActivity.this, viewPager2, adapter);
        GestureDetector mGestureDetector = new GestureDetector(this, myGestureLisener);
        View view = (View) findViewById(R.id.v_gesture);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }

        });

    }

    @Override
    public void onVideoGet(List<VideoInfo> videoInfos) {
        initViewPager(videoInfos);
        backGroundView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.INVISIBLE);
        setSystemUIVisible(false);
        mPresenter.startDetector(R.id.main_container, R.layout.tfe_ic_camera_connection_fragment);
        initDetector();
    }

    @Override
    public void onGestureGet(Gesture gesture) {
        switch (gesture.gesture) {
            case ResultHandler.GESTURE_UP: {
                Log.i("Gesture", "up");
                Toast.makeText(MainActivity.this, "上滑", Toast.LENGTH_LONG).show();
                myGestureLisener.gestureUp();
                break;
            }
            case ResultHandler.GESTURE_DOWN: {
                Log.i("Gesture", "down");
                Toast.makeText(MainActivity.this, "下滑", Toast.LENGTH_LONG).show();
                myGestureLisener.gestureDown();
                break;
            }
            case ResultHandler.GESTURE_OK: {
                Log.i("Gesture", "ok");
                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
                myGestureLisener.gestureOK();
                break;
            }
            case ResultHandler.GESTURE_PALM: {
                Log.i("Gesture", "palm");
                Toast.makeText(MainActivity.this, "palm", Toast.LENGTH_LONG).show();
                myGestureLisener.gesturePalm();
                break;
            }
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String errMessage) {

    }

    @Override
    public void onGestureUp() {
        myGestureLisener.gestureUp();
    }


    @Override
    public void onGestureDown() {
        myGestureLisener.gestureDown();
    }

    @Override
    public void onGestureOk() {
        myGestureLisener.gestureOK();
    }

    @Override
    public void onGesturePalm() {
        myGestureLisener.gesturePalm();
    }






}
