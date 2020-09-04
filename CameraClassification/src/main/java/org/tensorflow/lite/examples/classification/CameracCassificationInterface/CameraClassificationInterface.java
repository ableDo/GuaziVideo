package org.tensorflow.lite.examples.classification.CameracCassificationInterface;
import android.app.Activity;
public interface CameraClassificationInterface {

    public void startGestureDetect(ResultHandler handler, Activity obj, int id, int layout);
    public void endGestureDetect();
}
