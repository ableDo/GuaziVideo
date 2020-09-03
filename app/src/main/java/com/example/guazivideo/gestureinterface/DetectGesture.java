package com.example.guazivideo.gestureinterface;

import android.view.View;

import com.example.guazivideo.activity.BaseActivity;

import org.tensorflow.lite.examples.classification.ClassifierActivity;
import org.tensorflow.lite.examples.classification.GestureInterface.GestureHandler;

public class DetectGesture implements DetectGestureInterface {
    private ClassifierActivity pC;


    @Override
    public void startGestureDetect(GestureHandler handler, BaseActivity obj, int id, int layout) {
        pC = new ClassifierActivity(handler, obj, id, layout);
        pC.startGestureDetect();
    }

    @Override
    public void endGestureDetect() {
        pC.endGestureDetect();
    }
}
