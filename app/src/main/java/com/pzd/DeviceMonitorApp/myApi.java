package com.pzd.DeviceMonitorApp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface myApi
{
    @POST("status")

    Call<Model> createPost(@Body Model myModel);

}
