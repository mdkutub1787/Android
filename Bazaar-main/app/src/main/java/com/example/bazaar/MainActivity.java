package com.example.bazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.example.bazaar.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(view -> {
            startActivity(new Intent(this,AddProductActivity.class));
        });

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.home){
                    showHomeFragment();
                    return  true;
                } else if (id== R.id.chat) {
                    showChatFragment();
                    return  true;

                } else if (id == R.id.fav) {
                    showFavFragment();
                    return  true;

                } else if (id == R.id.account) {
                    showAccountFragment();
                    return  true;

                }else {
                    return false;
                }

            }
        });
    }

    private void showHomeFragment() {
        binding.toolbarTitle.setText("Home");
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(),fragment,"HomeFragment");
        transaction.commit();
    }

    private void showChatFragment(){
        binding.toolbarTitle.setText("Chat");
        ChatFragment fragment = new ChatFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(),fragment,"ChatFragment");
        transaction.commit();
    }

    private void showFavFragment(){
        binding.toolbarTitle.setText("Favourite");
        FavFragment fragment = new FavFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(),fragment,"FavFragment");
        transaction.commit();
    }

    private void showAccountFragment(){
        binding.toolbarTitle.setText("Account");
        AccountFragment fragment = new AccountFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(),fragment,"AccountFragment");
        transaction.commit();
    }


}
//Create an empty activity named LoginOptionActivity
