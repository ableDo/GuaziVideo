package com.example.guazivideo.entity;

import java.io.Serializable;

public class ShortVideoInfo implements Serializable {
    Video video_1;
    Video video_2;
    Video video_3;
    Video video_4;

    public Video getVideo_1() {
        return video_1;
    }

    public void setVideo_1(Video video_1) {
        this.video_1 = video_1;
    }

    public Video getVideo_2() {
        return video_2;
    }

    public void setVideo_2(Video video_2) {
        this.video_2 = video_2;
    }

    public Video getVideo_3() {
        return video_3;
    }

    public void setVideo_3(Video video_3) {
        this.video_3 = video_3;
    }

    public Video getVideo_4() {
        return video_4;
    }

    public void setVideo_4(Video video_4) {
        this.video_4 = video_4;
    }

    @Override
    public String toString() {
        return "ShortVideoInfo{" +
                "video_1=" + video_1 +
                ", video_2=" + video_2 +
                ", video_3=" + video_3 +
                ", video_4=" + video_4 +
                '}';
    }
}
