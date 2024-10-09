package com.example.jeeapp.api;


import com.example.jeeapp.model.PurbachalModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public class PurbachalClubApi {

    @GET("purbachal.php?action=read")
    Call<List<PurbachalModel>> getPurbachalModels();
}
