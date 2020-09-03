package com.example.guazivideo.gestureinterface;

import com.example.guazivideo.activity.MainActivity;

public interface DetectGestureInterface {

    public void startGestureDetect(GestureHandler handler, MainActivity obj);
    public void endGestureDetect();
}
