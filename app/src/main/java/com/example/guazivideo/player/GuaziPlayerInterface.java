package com.example.guazivideo.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface GuaziPlayerInterface {

    void clickOnce();

    void doubleClick();

    void release();

    void changeSourceAndPlay(String url, String title, boolean isBackBtnVisible, boolean cache);
}
