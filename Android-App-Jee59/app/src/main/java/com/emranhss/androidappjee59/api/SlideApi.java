package com.emranhss.androidappjee59.api;

import com.emranhss.androidappjee59.model.Slide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SlideApi {

    @GET("get_slides.php")
    Call<List<Slide>> getSlides();

}
