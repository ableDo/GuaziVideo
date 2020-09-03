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
import site.gemus.openingstartanimation.NormalDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.guazivideo.HorizontalVpAdapter;
import com.example.guazivideo.R;
import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.gestureinterface.DetectGesture;
import com.example.guazivideo.gestureinterface.GestureHandler;
import com.example.guazivideo.player.GuaziPlayer;
import com.example.guazivideo.request.VideoService;
import com.example.guazivideo.request.WebService;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int TIMER = 999;
    private static boolean flag = true;
    private ViewPager2 viewPager2;
    private HorizontalVpAdapter adapter;
    private UnVisibleHandler unVisibleHandler;
    private static final int START_TIME = 4000;

    private Toast mytoast;

    private void showToast(String text) {
        if (mytoast == null) {
            mytoast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        }  else {
            mytoast.setText(text);
        }
        mytoast.show();
    }
    private void startGestureDetect() {
        new DetectGesture().startGestureDetect(new GestureHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case GestureHandler.GESTURE_DOWN: {
                        gestureDown();
                        break;
                    }
                    case GestureHandler.GESTURE_UP: {
                        gestureUp();
                        break;
                    }
                    case GestureHandler.GESTURE_FAVOR: {
                        gestureFavor();
                        break;
                    }
                    case GestureHandler.GESTURE_RIGHT: {
                        gestureRight();
                        break;
                    }
                }
            }
        }, MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGestureDetect();
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setDrawStategy(new LineDrawStrategy()) //设置动画效果
                .setAnimationFinishTime(START_TIME)
                .create();
        openingStartAnimation.show(this);
        //隐藏状态栏和导航栏
        setTimer();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
        GuaziPlayer videoPlayer = recyclerView.getChildAt(0).findViewById(R.id.video_player);
        videoPlayer.clickOnce();
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
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
                    gestureRight();
                }
                //下滑
                if (velocityY > 0 && Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
                    gestureDown();
                }
                //左滑
                if (velocityX < 0 && Math.abs(velocityX) > 2 * Math.abs(velocityY)) {
                    Log.i("Gesture", "left");
                    getstureLeft();
                }
                //上滑
                if (velocityY < 0 && Math.abs(velocityY) > 2 * Math.abs(velocityX)) {
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

                    if (flag) {
                        Message message = this.obtainMessage(TIMER);
                        this.sendMessageDelayed(message, 1000);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unVisibleHandler.removeCallbacksAndMessages(null);
    }

    private void gestureFavor() {
        Log.i("Gesture", "favor");
        showToast("喜欢");
        //Toast.makeText(MainActivity.this, "喜欢", Toast.LENGTH_LONG).show();

    }

    private void gestureUp() {
        Log.i("Gesture", "up");
        if (adapter.getItemCount() > viewPager2.getCurrentItem() + 1) {
            adapter.setTempPosition(viewPager2.getCurrentItem() + 1);
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
            adapter.notifyDataSetChanged();
        }
        showToast("上滑");
       // Toast.makeText(MainActivity.this, "上滑", Toast.LENGTH_LONG).show();

    }

    private void gestureDown() {
        Log.i("Gesture", "down");
        if (viewPager2.getCurrentItem() > 0) {
            adapter.setTempPosition(viewPager2.getCurrentItem() - 1);
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1, true);
            adapter.notifyDataSetChanged();

        }
        showToast("下滑");
        //Toast.makeText(MainActivity.this, "下滑", Toast.LENGTH_LONG).show();

    }



    private void gestureRight() {
        Log.i("Gesture", "right");
        Toast.makeText(MainActivity.this, "右滑", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, MoreInformationActivity.class);
        intent.putExtra("video_info", adapter.getVideoInfoByPosition(viewPager2.getCurrentItem()));
        startActivity(intent);
    }

    private void getstureLeft() {
        Log.i("Gesture", "left");
        Toast.makeText(MainActivity.this, "左滑", Toast.LENGTH_LONG).show();

    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            Log.i("gesture" ,  " ");

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
                            gestureUp();
                            break;
                        }
                        case GestureHandler.GESTURE_DOWN: {
                            gestureDown();
                            break;
                        }
                        case GestureHandler.GESTURE_FAVOR: {
                            gestureFavor();
                            break;
                        }
                        case GestureHandler.GESTURE_RIGHT: {
                            gestureRight();
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
