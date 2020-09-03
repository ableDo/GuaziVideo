package com.example.guazivideo.request;

import com.example.guazivideo.entity.VideoInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VideoService {
    @GET("get")
    Call<List<VideoInfo>> getVideo();
}