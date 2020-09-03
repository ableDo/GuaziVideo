package com.example.guazivideo.gestureinterface;

import com.example.guazivideo.activity.MainActivity;

import org.tensorflow.lite.examples.classification.ClassifierActivity;

public class DetectGesture implements DetectGestureInterface {
    private ClassifierActivity pC;
    @Override
    public void startGestureDetect(GestureHandler handler, MainActivity obj) {
        pC = new ClassifierActivity(handler, obj);
        pC.startGestureDetect();
    }

    @Override
    public void endGestureDetect() {
        pC.endGestureDetect();
    }
}
