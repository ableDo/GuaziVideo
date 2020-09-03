package com.example.guazivideo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {
    @GET("getgesture")
    Call<Gesture> getGesture();
}
