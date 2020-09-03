package com.example.guazivideo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.guazivideo.adapter.HorizontalVpAdapter;
import com.example.guazivideo.R;
import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.gestureinterface.DetectGesture;
import com.example.guazivideo.gestureinterface.GestureHandler;
import com.example.guazivideo.player.GuaziPlayer;
import com.example.guazivideo.request.VideoService;
import com.example.guazivideo.request.WebService;
import com.example.guazivideo.view.RotationLoadingView;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int TIMER = 999;
    private static boolean flag = true;
    private ViewPager2 viewPager2;
    private HorizontalVpAdapter adapter;
    private UnVisibleHandler unVisibleHandler;
    private static final int START_TIME = 2000;
    private boolean isFullVideo = false;
    private boolean isGestureOpen = true;
    private boolean isGesturesolving = false;
    DetectGesture myDetectGesture = null;

    private View backGroundView;
    private RotationLoadingView loadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTimer();
        initView();
        initData();
        initDetector();
        handler.postDelayed(task, 2000);//立即调用

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);
        videoPlayer.clickOnce();
        setSystemUIVisible(false);
        if (myDetectGesture != null) {
            startGestureDetect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);
        videoPlayer.clickOnce();
        myDetectGesture.endGestureDetect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unVisibleHandler.removeCallbacksAndMessages(null);
        stopTimer();
    }


    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://2ed85251-2a97-4233-ac7d-fc93226486fb.mock.pstmn.io/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        VideoService service = retrofit.create(VideoService.class);

        Call<List<VideoInfo>> call = service.getVideo();
        call.enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                List<VideoInfo> videoInfos = response.body();
                initViewPager(videoInfos);
                backGroundView.setVisibility(View.INVISIBLE);
                loadingView.setVisibility(View.INVISIBLE);
                setSystemUIVisible(false);
                startGestureDetect();
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initView() {
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

    private void initDetector() {
        GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.i("Gesture", "favor");
                Toast.makeText(MainActivity.this, "喜欢", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MainActivity.this, "右滑", Toast.LENGTH_LONG).show();
                    gestureRight();
                }
                //下滑
                if (velocityY > 0 && Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
                    Log.i("Gesture", "down");
                    Toast.makeText(MainActivity.this, "下滑", Toast.LENGTH_LONG).show();
                    gestureDown();
                }
                //左滑
                if (velocityX < 0 && Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
                    Log.i("Gesture", "left");
                    Toast.makeText(MainActivity.this, "左滑", Toast.LENGTH_LONG).show();
                    getstureLeft();
                }
                //上滑
                if (velocityY < 0 && Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
                    Log.i("Gesture", "up");
                    Toast.makeText(MainActivity.this, "上滑", Toast.LENGTH_LONG).show();
                    gestureUp();
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

    private void setTimer() {
        unVisibleHandler = new UnVisibleHandler(MainActivity.this);
        Message message = unVisibleHandler.obtainMessage(TIMER);     // Message
        unVisibleHandler.sendMessageDelayed(message, START_TIME);
    }

    private static class UnVisibleHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

        public UnVisibleHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mActivity.get() == null) {
                return;
            }
            switch (msg.what) {
                case TIMER:
                    mActivity.get().setSystemUIVisible(false);
                    break;
                default:
                    break;
            }
        }
    }

    ;

    private void stopTimer() {
        flag = false;
    }

    private void gestureFavor() {

    }
    private void gestureOK() {
        getstureLeft();
    }
    private void gesturePalm() {
        gestureRight();
    }

    private void gestureUp() {
        if (adapter.getItemCount() > viewPager2.getCurrentItem() + 1 && !isFullVideo && !isGesturesolving) {
            isGesturesolving = true;
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.animation_up);
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

    private void gestureDown() {

        if (viewPager2.getCurrentItem() > 0 && !isFullVideo && !isGesturesolving) {
            isGesturesolving = true;
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.animation_down);
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



    private void gestureRight() {
        if (isGesturesolving) {
            return;
        }
        isGesturesolving = true;
        if (isFullVideo) {
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.animation_right);
            videoPlayer.startAnimation(animation);

            VideoInfo videoInfo = adapter.getVideoInfoByPosition(viewPager2.getCurrentItem());
            videoPlayer.release();
            videoPlayer.changeSourceAndPlay(videoInfo.getShort_video_info().getVideo_1().getUrl(), videoInfo.getShort_title(), false);
            isFullVideo = false;
        } else {
            //进入详情页
            Intent intent = new Intent(MainActivity.this, MoreInformationActivity.class);
            intent.putExtra("video_info", adapter.getVideoInfoByPosition(viewPager2.getCurrentItem()));
            startActivity(intent);
            overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
        }
        isGesturesolving  = false;
    }

    private void getstureLeft() {
        if (isGesturesolving) {
            return;
        }
        isGesturesolving = true;
        if (!isFullVideo)  {
            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);

            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.animation_left);
            videoPlayer.startAnimation(animation);

            VideoInfo videoInfo = adapter.getVideoInfoByPosition(viewPager2.getCurrentItem());
            videoPlayer.release();
            videoPlayer.changeSourceAndPlay(videoInfo.getMovie_video_info().getVideo_1().getUrl(), videoInfo.getMovie_name(), true);
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
    @SuppressLint("HandlerLeak")
    private void startGestureDetect() {
        if (myDetectGesture == null) {
            myDetectGesture = new DetectGesture();
        }
        myDetectGesture.startGestureDetect(new GestureHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case GestureHandler.GESTURE_DOWN: {
                        if (isGestureOpen) {
                            Log.i("Gesture", "down");
                            Toast.makeText(MainActivity.this, "下滑", Toast.LENGTH_LONG).show();
                            gestureDown();
                        }
                        break;
                    }
                    case GestureHandler.GESTURE_UP: {
                        if (isGestureOpen) {
                            Log.i("Gesture", "up");
                            Toast.makeText(MainActivity.this, "上滑", Toast.LENGTH_LONG).show();
                            gestureUp();
                        }
                        break;
                    }
                    case GestureHandler.GESTURE_OK: {
                        Log.i("Gesture", "ok");
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
                        gestureOK();
                        break;
                    }
                    case GestureHandler.GESTURE_PALM: {
                        if (isGestureOpen) {
                            Log.i("Gesture", "palm");
                            Toast.makeText(MainActivity.this, "palm", Toast.LENGTH_LONG).show();
                            gesturePalm();
                        }
                        break;
                    }
                }
            }
        }, MainActivity.this, findViewById(R.id.main_container));
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub

            handler.postDelayed(this,  1000);//设置延迟时间，此处是5秒
            //需要执行的代码
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
                    switch (gesture.gesture) {
                        case GestureHandler.GESTURE_UP: {
                            Log.i("Gesture", "up");
                            Toast.makeText(MainActivity.this, "上滑", Toast.LENGTH_LONG).show();
                            gestureUp();
                            break;
                        }
                        case GestureHandler.GESTURE_DOWN: {
                            Log.i("Gesture", "down");
                            Toast.makeText(MainActivity.this, "下滑", Toast.LENGTH_LONG).show();
                            gestureDown();
                            break;
                        }
                        case GestureHandler.GESTURE_OK: {
                            Log.i("Gesture", "ok");
                            Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
                            gestureOK();
                            break;
                        }
                        case GestureHandler.GESTURE_PALM: {
                            Log.i("Gesture", "palm");
                            Toast.makeText(MainActivity.this, "palm", Toast.LENGTH_LONG).show();
                            gesturePalm();
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Gesture> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    };



}
