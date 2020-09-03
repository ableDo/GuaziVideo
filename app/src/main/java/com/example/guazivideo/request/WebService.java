package com.example.guazivideo.request;

import com.example.guazivideo.entity.Gesture;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {
  @GET("getgesture")
  Call<Gesture> getGesture();
}