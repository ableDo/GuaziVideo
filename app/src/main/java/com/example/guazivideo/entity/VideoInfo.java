package com.example.guazivideo.entity;

public class VideoInfo {
    String movie_id;
    String short_title;
    String short_author;
    String short_author_avatar_url;
    String short_cover;
    int short_like_count;
    int short_watch_count;
    int short_duration;
    String movie_name;
    int movie_duration;
    String movie_desc;
    String movie_cover;
    ShortVideoInfo short_video_info;
    MovieVideoInfo movie_video_info;


    public String getMovie_id() {
        return movie_id;
    }

    public String getShort_title() {
        return short_title;
    }

    public String getShort_author() {
        return short_author;
    }

    public String getShort_author_avatar_url() {
        return short_author_avatar_url;
    }

    public String getShort_cover() {
        return short_cover;
    }

    public int getShort_like_count() {
        return short_like_count;
    }

    public int getShort_watch_count() {
        return short_watch_count;
    }

    public int getShort_duration() {
        return short_duration;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public int getMovie_duration() {
        return movie_duration;
    }

    public String getMovie_desc() {
        return movie_desc;
    }

    public String getMovie_cover() {
        return movie_cover;
    }

    public ShortVideoInfo getShort_video_info() {
        return short_video_info;
    }

    public MovieVideoInfo getMovie_video_info() {
        return movie_video_info;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public void setShort_author(String short_author) {
        this.short_author = short_author;
    }

    public void setShort_author_avatar_url(String short_author_avatar_url) {
        this.short_author_avatar_url = short_author_avatar_url;
    }

    public void setShort_cover(String short_cover) {
        this.short_cover = short_cover;
    }

    public void setShort_like_count(int short_like_count) {
        this.short_like_count = short_like_count;
    }

    public void setShort_watch_count(int short_watch_count) {
        this.short_watch_count = short_watch_count;
    }

    public void setShort_duration(int short_duration) {
        this.short_duration = short_duration;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public void setMovie_duration(int movie_duration) {
        this.movie_duration = movie_duration;
    }

    public void setMovie_desc(String movie_desc) {
        this.movie_desc = movie_desc;
    }

    public void setMovie_cover(String movie_cover) {
        this.movie_cover = movie_cover;
    }

    public void setShort_video_info(ShortVideoInfo short_video_info) {
        this.short_video_info = short_video_info;
    }

    public void setMovie_video_info(MovieVideoInfo movie_video_info) {
        this.movie_video_info = movie_video_info;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "movie_id='" + movie_id + '\'' +
                ", short_title='" + short_title + '\'' +
                ", short_author='" + short_author + '\'' +
                ", short_author_avatar_url='" + short_author_avatar_url + '\'' +
                ", short_cover='" + short_cover + '\'' +
                ", short_like_count=" + short_like_count +
                ", short_watch_count=" + short_watch_count +
                ", short_duration=" + short_duration +
                ", movie_name='" + movie_name + '\'' +
                ", movie_duration=" + movie_duration +
                ", movie_desc='" + movie_desc + '\'' +
                ", movie_cover='" + movie_cover + '\'' +
                ", short_video_info=" + short_video_info +
                ", movie_video_info=" + movie_video_info +
                '}';
    }
}
