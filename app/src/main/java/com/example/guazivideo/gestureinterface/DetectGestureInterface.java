package com.example.guazivideo.gestureinterface;

import android.view.View;

import com.example.guazivideo.activity.BaseActivity;

import org.tensorflow.lite.examples.classification.GestureInterface.GestureHandler;

public interface DetectGestureInterface {

    public void startGestureDetect(GestureHandler handler, BaseActivity obj, int id, int layout);
    public void endGestureDetect();
}
