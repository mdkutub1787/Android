package com.example.bazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bazaar.databinding.ActivityLoginOptionBinding;

public class LoginOptionActivity extends AppCompatActivity {

    ActivityLoginOptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginOptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}

// add binding properties
// Now we need to create firebase project and Add dependencies