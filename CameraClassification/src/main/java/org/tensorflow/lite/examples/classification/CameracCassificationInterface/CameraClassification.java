package org.tensorflow.lite.examples.classification.CameracCassificationInterface;

import android.app.Activity;
import org.tensorflow.lite.examples.classification.ClassifierActivity;

public class CameraClassification implements  CameraClassificationInterface{
    private ClassifierActivity pC;


    @Override
    public void startGestureDetect(ResultHandler handler, Activity obj, int id, int layout) {
        pC = new ClassifierActivity(handler, obj, id, layout);
        pC.startGestureDetect();
    }

    @Override
    public void endGestureDetect() {
        pC.endGestureDetect();
    }
}
