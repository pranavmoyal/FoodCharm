package com.pranav.foodcharm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pranav.foodcharm.R;
import com.pranav.foodcharm.view.TextView;

public class SplashScreen extends AppCompatActivity {

    TextView tv_fdlovers;
    private Animation myAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        inIt();
    }

    private void inIt(){
        tv_fdlovers=(TextView) findViewById(R.id.tv_fdlovers);


        myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_text);
        tv_fdlovers.startAnimation(myAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 5000);
    }
}

