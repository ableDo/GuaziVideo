package com.example.guazivideo.mvp.contract;

import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.mvp.base.BaseView;

import java.util.List;

public interface MainContract {
    interface Model {
        void requestVideo();

    }

    interface View extends BaseView {

        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(String errMessage);

        void onGestureUp();

        void onGestureDown();

        void onGestureOk();

        void onGesturePalm();

        void initDetector();

        void onVideoGet(List<VideoInfo> videoInfos);

    }

    interface Presenter {

        void setTimer();

        void stopTimer();

        void startDetector(int id, int layout);

        void stopDetector();

        void requestVideo();

        void onVideoGet(List<VideoInfo> videoInfos);

    }
}
