package com.example.jeeapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.jeeapp.api.SlideApi;
import com.example.jeeapp.apiClient.ApiClient;
import com.example.jeeapp.model.Slide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SlideApi slideApi= ApiClient.getRetrofit().create(SlideApi.class);

        Call<List<Slide>> call = slideApi.getSlides();

        call.enqueue(new Callback<List<Slide>>() {
            @Override
            public void onResponse(Call<List<Slide>> call, Response<List<Slide>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Slide> slides = response.body();

                    ArrayList<SlideModel> imageList = new ArrayList<>();
                    for (Slide slide : slides) {

                        String relativeImagePath = slide.getImage();
                        String baseUrl = "https://purbachal.emranhss.com/";
                        String imageUrl = baseUrl + relativeImagePath;

                        imageList.add(new SlideModel(imageUrl, slide.getTitle(), ScaleTypes.CENTER_CROP));
                        System.out.println(slide.getImage());
                    }


                    ImageSlider imageSlider = findViewById(R.id.image_slider);
                    imageSlider.setImageList(imageList);
                }
            }

            @Override
            public void onFailure(Call<List<Slide>> call, Throwable t) {

            }
        });
    }
}