package com.example.guazivideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.guazivideo.R;
import com.example.guazivideo.entity.Changer;
import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.mvp.base.BaseActivity;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.net.WebService;

import org.tensorflow.lite.examples.classification.CameracCassificationInterface.CameraClassification;
import org.tensorflow.lite.examples.classification.CameracCassificationInterface.ResultHandler;


public class MoreInformationActivity extends BaseActivity {
    CameraClassification myDetectGesture = null;
    private boolean isAsk = true;
    private boolean isOpen = true;

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
        handler.post(task);

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
        isAsk = false;
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
                        if (isOpen) {
                            Log.i("Gesture", "palm");
                            Toast.makeText(MoreInformationActivity.this, "palm", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                    }
                }
            }
        }, MoreInformationActivity.this, R.id.more_container, R.layout.tfe_ic_camera_connection_fragment);
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub

            if (isAsk) {
                handler.postDelayed(this, 1000);//设置延迟时间，此处是5秒
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://39.106.7.119:8080/api/v1/user/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                        .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                        .build();

                WebService service = retrofit.create(WebService.class);

                Call<Gesture> call = service.getGesture();

                call.enqueue(new Callback<Gesture>() {
                    @Override
                    public void onResponse(Call<Gesture> call, Response<Gesture> response) {
                        Gesture gesture = response.body();
                        Log.i("gesture" , gesture.gesture + " ");
                        if (gesture.gesture == ResultHandler.GESTURE_PALM) {
                            Log.i("Gesture", "palm");
                            Toast.makeText(MoreInformationActivity.this, "palm", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Gesture> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


                Call<Changer> call1 = service.getChangerStates();

                call1.enqueue(new Callback<Changer>() {
                    @Override
                    public void onResponse(Call<Changer> call, Response<Changer> response) {
                        Changer changer = response.body();
                        Log.i("changer" , changer.isChangerOpen + " ");
                        isOpen = changer.isChangerOpen;
                    }

                    @Override
                    public void onFailure(Call<Changer> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        }
    };

}


