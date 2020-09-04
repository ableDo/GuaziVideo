package com.example.guazivideo.mvp.presenter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.guazivideo.R;
import com.example.guazivideo.activity.MainActivity;
import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;

import org.tensorflow.lite.examples.classification.CameracCassificationInterface.CameraClassification;
import org.tensorflow.lite.examples.classification.CameracCassificationInterface.CameraClassification;
import org.tensorflow.lite.examples.classification.CameracCassificationInterface.ResultHandler;

import com.example.guazivideo.mvp.base.BaseActivity;
import com.example.guazivideo.mvp.base.BasePresenter;
import com.example.guazivideo.mvp.contract.MainContract;
import com.example.guazivideo.mvp.model.MainModel;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private UnVisibleHandler unVisibleHandler;
    private MainContract.Model model;
    private static final int TIMER = 999;
    private static boolean flag = true;
    private static int duration = 500;
    private boolean isGestureOpen = true;
    CameraClassification myDetectGesture = null;
    Context mContext;


    public MainPresenter() {
        model = new MainModel(this);
    }

    @Override
    public void attachView(MainContract.View view) {
        mContext = (Context) view;
        mView = view;
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
    @SuppressLint("HandlerLeak")
    private void startGestureDetect(int id, int layout) {
        if (myDetectGesture == null) {
            myDetectGesture = new CameraClassification();
        }
        myDetectGesture.startGestureDetect(new ResultHandler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case ResultHandler.GESTURE_DOWN: {
                        if (isGestureOpen) {
                            Log.i("Gesture", "down");
                            Toast.makeText(mContext, "下滑", Toast.LENGTH_LONG).show();
                            mView.onGestureDown();
                        }
                        break;
                    }
                    case ResultHandler.GESTURE_UP: {
                        if (isGestureOpen) {
                            Log.i("Gesture", "up");
                            Toast.makeText(mContext, "上滑", Toast.LENGTH_LONG).show();
                            mView.onGestureUp();
                        }
                        break;
                    }
                    case ResultHandler.GESTURE_OK: {
                        Log.i("Gesture", "ok");
                        Toast.makeText(mContext, "ok", Toast.LENGTH_LONG).show();
                        mView.onGestureOk();
                        break;
                    }
                    case ResultHandler.GESTURE_PALM: {
                        if (isGestureOpen) {
                            Log.i("Gesture", "palm");
                            Toast.makeText(mContext, "palm", Toast.LENGTH_LONG).show();
                            mView.onGesturePalm();
                        }
                        break;
                    }
                }
            }
        }, (BaseActivity) mContext, id, layout);
    }

    public void setTimer() {
        unVisibleHandler = new UnVisibleHandler((MainActivity) mView);
        Message message = unVisibleHandler.obtainMessage(TIMER);     // Message
        unVisibleHandler.sendMessageDelayed(message, MainActivity.START_TIME);
    }


    public void stopTimer() {
        flag = false;
    }

    @Override
    public void startDetector(int id, int layout) {
        startGestureDetect(id, layout);
    }

    @Override
    public void stopDetector() {
        myDetectGesture.endGestureDetect();
    }

    @Override
    public void requestVideo() {
        model.requestVideo();
    }

    @Override
    public void startRequestChangerAndGesture() {
        handler.postDelayed(task, 2000);
    }

    @Override
    public void onVideoGet(List<VideoInfo> videoInfos) {
        mView.onVideoGet(videoInfos);
    }

    @Override
    public void onrequestChangerStatesGet(boolean isOpen) {
        isGestureOpen = isOpen;
    }

    @Override
    public void onGestureGet(Gesture gesture) {
        mView.onGestureGet(gesture);
    }

    @Override
    public void detachView() {
        super.detachView();
        unVisibleHandler.removeCallbacksAndMessages(null);
        stopTimer();
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub

            handler.postDelayed(this, duration);//设置延迟时间，此处是5秒
            //需要执行的代码
            model.requestChangerAndGesture();
        }
    };
}
