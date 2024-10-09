package com.emranhss.androidappjee59.api;

import com.emranhss.androidappjee59.model.NotificationModel;
import com.emranhss.androidappjee59.model.PurbachalModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PurbachalClubApi {

    @GET("purbachal.php?action=read")
    Call<List<PurbachalModel>> getPurbachalModels();

}
