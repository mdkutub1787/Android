package com.emranhss.androidappjee59;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emranhss.androidappjee59.adapter.NotificationAdapter;
import com.emranhss.androidappjee59.api.NotificationApi;
import com.emranhss.androidappjee59.api.SlideApi;
import com.emranhss.androidappjee59.apiClient.ApiClient;
import com.emranhss.androidappjee59.model.NotificationModel;
import com.emranhss.androidappjee59.model.Slide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {


    private RecyclerView notificationList;
    private NotificationAdapter notificationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notificationList=findViewById(R.id.noticeList);

        notificationList.setLayoutManager(new LinearLayoutManager(this));

        NotificationApi notificationApi = ApiClient.getRetrofit().create(NotificationApi.class);

        Call<List<NotificationModel>> call = notificationApi.getNotifications();


        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {

                if (response.isSuccessful()) {
                    List<NotificationModel> noticeList = response.body();

                    // Set up RecyclerView with the adapter
                    notificationAdapter = new NotificationAdapter(noticeList, getApplicationContext() );
                    notificationList.setAdapter(notificationAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {

            }
        });






    }
}