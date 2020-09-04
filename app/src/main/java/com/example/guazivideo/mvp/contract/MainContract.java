package com.example.guazivideo.mvp.contract;

import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;
import com.example.guazivideo.mvp.base.BaseView;

import java.util.List;

public interface MainContract {
    interface Model {
        void requestVideo();

        void requestChangerAndGesture();

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

        void onGestureGet(Gesture gesture);

    }

    interface Presenter {

        void setTimer();

        void stopTimer();

        void startDetector(int id, int layout);

        void stopDetector();

        void requestVideo();

        void startRequestChangerAndGesture();

        void onVideoGet(List<VideoInfo> videoInfos);

        void onrequestChangerStatesGet(boolean isOpen);

        void onGestureGet(Gesture gesture);

    }
}
