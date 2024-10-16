package com.towhid.wetherapp;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class Dhaka extends AppCompatActivity {

    private WebView dhaka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka);

        dhaka = findViewById(R.id.dhaka);

        WebSettings webSettings = dhaka.getSettings();
        dhaka.setWebViewClient(new SameView());
        webSettings.setJavaScriptEnabled(true);
        dhaka.loadUrl("https://www.timeanddate.com/weather/@1337179/ext");
    }

    public class SameView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, android.webkit.WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
