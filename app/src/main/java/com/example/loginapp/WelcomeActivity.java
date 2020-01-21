package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity
{
    ImageView splash;
    Animation animation;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

       splash = findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(this,R.anim.splash_animation);

       splash.setAnimation(animation);

        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {

                Intent intent  = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
