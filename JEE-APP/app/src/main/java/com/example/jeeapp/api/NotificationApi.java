package com.example.jeeapp.api;

import com.example.jeeapp.model.NotificationModel;
import com.example.jeeapp.model.Slide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotificationApi {


    @GET("get_notices.php")
    Call<List<NotificationModel>> getNotifications();
}