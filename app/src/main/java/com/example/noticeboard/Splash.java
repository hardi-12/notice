package com.example.noticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;

public class Splash extends AppCompatActivity {
    Animation animation;
    LottieAnimationView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    animation= AnimationUtils.loadAnimation(Splash.this,R.anim.annimation_1);
                    l=findViewById(R.id.l);
                    l.setAnimation(animation);
                    Thread.sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                Intent i=new Intent(Splash.this,IntroActivity.class);
                startActivity(i);
                finish();




            }
        }).start();
    }
}