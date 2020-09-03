package com.example.guazivideo.gestureinterface;

import android.view.View;

import com.example.guazivideo.activity.BaseActivity;
import com.example.guazivideo.activity.MainActivity;

public interface DetectGestureInterface {

    public void startGestureDetect(GestureHandler handler, BaseActivity obj, View view);
    public void endGestureDetect();
}
