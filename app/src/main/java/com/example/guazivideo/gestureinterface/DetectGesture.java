package com.example.guazivideo.gestureinterface;

import android.view.View;

import com.example.guazivideo.activity.BaseActivity;
import com.example.guazivideo.activity.MainActivity;

import org.tensorflow.lite.examples.classification.ClassifierActivity;

public class DetectGesture implements DetectGestureInterface {
    private ClassifierActivity pC;


    @Override
    public void startGestureDetect(GestureHandler handler, BaseActivity obj, View view) {
        pC = new ClassifierActivity(handler, obj, view);
        pC.startGestureDetect();
    }

    @Override
    public void endGestureDetect() {
        pC.endGestureDetect();
    }
}
