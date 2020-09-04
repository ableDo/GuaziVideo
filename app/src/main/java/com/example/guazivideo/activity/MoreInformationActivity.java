package com.example.guazivideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.guazivideo.R;
import com.example.guazivideo.mvp.base.BaseActivity;
import com.example.guazivideo.entity.VideoInfo;
import org.tensorflow.lite.examples.classification.CameracCassificationInterface.CameraClassification;
import org.tensorflow.lite.examples.classification.CameracCassificationInterface.ResultHandler;


public class MoreInformationActivity extends BaseActivity {
    CameraClassification myDetectGesture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);

//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        setSystemUIVisible(false);

        initDetector();
        startGestureDetect();

        Intent intent = getIntent();
        VideoInfo videoInfo = (VideoInfo) intent.getSerializableExtra("video_info");

        ImageView imageView_cover = (ImageView)findViewById(R.id.cover);
        ImageView imageView_author_avatar = (ImageView)findViewById(R.id.author_avatar);
        TextView textView_short_author = (TextView)findViewById(R.id.short_author);
        TextView textView_short_title = (TextView)findViewById(R.id.short_title);
        TextView textView_movie_name = (TextView)findViewById(R.id.movie_name);
        TextView textView_movie_desc = (TextView)findViewById(R.id.movie_desc);

        Glide.with(this).load(videoInfo.getMovie_cover()).into(imageView_cover);
        Glide.with(this).load(videoInfo.getShort_author_avatar_url()).into(imageView_author_avatar);

        textView_short_author.setText(videoInfo.getShort_author());
        textView_short_title.setText(videoInfo.getShort_title());
        textView_movie_name.setText(videoInfo.getMovie_name());
        textView_movie_desc.setText(videoInfo.getMovie_desc());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_more_information;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        myDetectGesture.endGestureDetect();
    }

    private void initDetector() {
        GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("MyGesture", "onFling" + velocityX + " " + velocityY);
                //左滑
                if (velocityX < 0 && Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
                    Log.i("Gesture", "left");
                    Animation animation = AnimationUtils.loadAnimation(MoreInformationActivity.this,R.anim.animation_left);
                    View view = findViewById(R.id.more_info_scroll);
                    view.startAnimation(animation);
                    finish();

                }
                return true;
            }
        });
        View view = findViewById(R.id.more_info_scroll);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }

        });
    }

    @SuppressLint("HandlerLeak")
    private void startGestureDetect() {
        if (myDetectGesture == null) {
            myDetectGesture = new CameraClassification();
        }
        myDetectGesture.startGestureDetect(new ResultHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {

                    case ResultHandler.GESTURE_PALM: {
                        Log.i("Gesture", "palm");
                        Toast.makeText(MoreInformationActivity.this, "palm", Toast.LENGTH_LONG).show();

                        break;
                    }
                }
            }
        }, MoreInformationActivity.this, R.id.more_container, R.layout.tfe_ic_camera_connection_fragment);
    }

}


