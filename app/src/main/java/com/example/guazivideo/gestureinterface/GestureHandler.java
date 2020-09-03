package com.example.guazivideo.gestureinterface;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class GestureHandler extends Handler {

    public static final int GESTURE_NORMAL = 1000;
    public static final int GESTURE_UP = 1001;
    public static final int GESTURE_DOWN = 1002;
    public static final int GESTURE_RIGHT = 1003;
    public static final int GESTURE_FAVOR = 1004;

}
