package com.example.guazivideo.entity;

import java.io.Serializable;

public class Video implements Serializable {
    String quality;
    int width;
    int height;
    long size;
    String url;

    public String getQuality() {
        return quality;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Video{" +
                "quality='" + quality + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", size=" + size +
                ", url='" + url + '\'' +
                '}';
    }
}
