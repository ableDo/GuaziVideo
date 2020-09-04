package com.example.guazivideo.mvp.model;



import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.guazivideo.R;
import com.example.guazivideo.activity.MainActivity;
import com.example.guazivideo.entity.Changer;
import com.example.guazivideo.entity.Gesture;
import com.example.guazivideo.entity.VideoInfo;

import org.tensorflow.lite.examples.classification.CameracCassificationInterface.ResultHandler;
import com.example.guazivideo.mvp.contract.MainContract;
import com.example.guazivideo.net.VideoService;
import com.example.guazivideo.net.WebService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainModel implements MainContract.Model {

    MainContract.Presenter mPresent;

    public MainModel(MainContract.Presenter mPresent) {
        this.mPresent = mPresent;
    }

    @Override
    public void requestVideo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://2ed85251-2a97-4233-ac7d-fc93226486fb.mock.pstmn.io/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        VideoService service = retrofit.create(VideoService.class);

        Call<List<VideoInfo>> call = service.getVideo();
        call.enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                List<VideoInfo> videoInfos = response.body();
                mPresent.onVideoGet(videoInfos);

            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void requestChangerAndGesture() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://39.106.7.119:8080/api/v1/user/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        WebService service = retrofit.create(WebService.class);

        Call<Gesture> call = service.getGesture();

        call.enqueue(new Callback<Gesture>() {
            @Override
            public void onResponse(Call<Gesture> call, Response<Gesture> response) {
                Gesture gesture = response.body();
                Log.i("gesture" , gesture.gesture + " ");
                mPresent.onGestureGet(gesture);
            }

            @Override
            public void onFailure(Call<Gesture> call, Throwable t) {
                t.printStackTrace();
            }
        });


        Call<Changer> call1 = service.getChangerStates();

        call1.enqueue(new Callback<Changer>() {
            @Override
            public void onResponse(Call<Changer> call, Response<Changer> response) {
                Changer changer = response.body();
                mPresent.onrequestChangerStatesGet(changer.isChangerOpen);
                Log.i("changer" , changer.isChangerOpen + " ");
            }

            @Override
            public void onFailure(Call<Changer> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


}
