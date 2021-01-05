package com.example.whatsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

public class LoadingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView splashGif = findViewById(R.id.splash);

        DrawableImageViewTarget gifImage = new DrawableImageViewTarget(splashGif);
        Glide.with(this).load(R.raw.splash).into(splashGif);

        startLoading();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent intent = new Intent(getBaseContext(), Login_Activity.class);
                startActivity(intent);
                finish();
            }
        }, 1200);
    }
}