package com.example.guazivideo.gestureinterface;

import com.example.guazivideo.activity.MainActivity;

import org.tensorflow.lite.examples.classification.ClassifierActivity;

public class DetectGesture implements DetectGestureInterface {

    @Override
    public void startGestureDetect(GestureHandler handler, MainActivity obj) {
        new ClassifierActivity(handler, obj).startGestureDetect();
    }

}
