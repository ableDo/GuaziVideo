package com.example.guazivideo.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guazivideo.R;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.player.GuaziPlayer;


public class MoreInformationActivity extends AppCompatActivity {

    private void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

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

//        initDetector();


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

    private void initDetector() {
        GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("MyGesture", "onFling" + velocityX + " " + velocityY);
                //左滑
                if (velocityX < 0 && Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
                    Log.i("Gesture", "left");
                    finish();
                }
                return true;
            }
        });
        View view = (View) findViewById(R.id.v_gesture);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }

        });
    }

}


